package com.universidadquinto.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.universidadquinto.dto.CourseDto;
import com.universidadquinto.entity.Course;
import com.universidadquinto.entity.User;
import com.universidadquinto.enums.TurnoEnum;
import com.universidadquinto.repository.CourseRepository;
import com.universidadquinto.repository.UserRepository;
import com.universidadquinto.service.CourseService;
import com.universidadquinto.service.UserService;

import jakarta.transaction.Transactional;

@Component
public class CourseServiceImpl implements CourseService {

	private CourseRepository courseRepository;
	private UserService userService;
	private UserRepository userRepository;

	public CourseServiceImpl(CourseRepository courseRepository, UserService userService,
			UserRepository userRepository) {
		this.courseRepository = courseRepository;
		this.userService = userService;
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public void saveCourse(String name, String turno, String startTime, String professorId) throws Exception {

		Course newCourse = new Course();
		newCourse.setName(name);

		if (professorId != null && !professorId.isEmpty()) {
			newCourse.setProfessor(userService.checkProfessorById(professorId));
		}

		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
		Date date = null;
		try {
			date = sdf.parse(startTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		newCourse.setStartTime(date);

		for (TurnoEnum t : TurnoEnum.values()) {
			if (t.name().equalsIgnoreCase(turno))
				newCourse.setTurno(t);
		}

		courseRepository.save(newCourse);
	}

	private CourseDto convertEntityToDto(Course course) {
		CourseDto courseDto = new CourseDto();
		courseDto.setId(course.getId());
		courseDto.setName(course.getName());
		courseDto.setTurno(getTurno(course.getTurno()));

		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(course.getStartTime());
		courseDto.setStart(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
		try {
			courseDto.setProfessor(userService.checkProfessorById(course.getProfessor().getId()).getName() + ' '
					+ userService.checkProfessorById(course.getProfessor().getId()).getLastname());
		} catch (Exception e) {
			courseDto.setProfessor("Sin profesor asignado.");
		}

		List<User> students = course.getStudents();
		courseDto.setStudents(
				students.stream().map((user) -> userService.convertEntityToDto(user)).collect(Collectors.toList()));

		return courseDto;
	}

	@Override
	@Transactional
	public void deleteCourse(Long id) throws Exception {
		try {
			courseRepository.deleteById(id);
		} catch (Exception e) {
			throw new Exception("No se pudo eliminar.");
		}

	}

	@Override
	@Transactional
	public CourseDto updateCourse(String name, String turno, String startTime, String professorId, Long id)
			throws Exception {

		Optional<Course> exist = courseRepository.findById(id);

		if (exist.isPresent()) {
			Course updated = exist.get();

			if (name != null && !name.isEmpty()) {
				updated.setName(name);
			}

			if (professorId != null && !professorId.isEmpty()) {
				updated.setProfessor(userService.checkProfessorById(professorId));
			}

			if (startTime != null && !startTime.isEmpty()) {
				SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
				Date date = null;
				try {
					date = sdf.parse(startTime);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				updated.setStartTime(date);
			}

			if (turno != null && !turno.isEmpty()) {
				for (TurnoEnum t : TurnoEnum.values()) {
					if (t.name().equalsIgnoreCase(turno))
						updated.setTurno(t);
				}
			}

			courseRepository.save(updated);

			return convertEntityToDto(updated);
		} else {
			throw new Exception("No se puedo actualizar.");
		}
	}

	@Override
	public CourseDto findByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CourseDto findById(Long id) {
		Optional<Course> exist = courseRepository.findById(id);
		if (exist.isPresent()) {
			return convertEntityToDto(exist.get());
		}
		return null;
	}

	@Override
	public List<CourseDto> findAllCourses() {
		List<Course> courses = courseRepository.findAll();
		return courses.stream().map((course) -> convertEntityToDto(course)).collect(Collectors.toList());
	}

	private String getTurno(TurnoEnum turno) {
		switch (turno) {
		case MORNING:
			return "Mañana";
		case AFTERNOON:
			return "Tarde";
		case NIGHT:
			return "Noche";
		default:
			return "Sin turno asignado";
		}
	}

	@Override
	public List<CourseDto> findStudentCourses(String id) {
		List<Course> courses = userRepository.findById(id).get().getCourses();
		return courses.stream().map((course) -> convertEntityToDto(course)).collect(Collectors.toList());
	}

	@Override
	public List<CourseDto> findNoStudentCourses(String id) {
		List<Course> courses = courseRepository.findNoStudentCourses(id);
		return courses.stream().map((course) -> convertEntityToDto(course)).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public CourseDto setStudent(Long courseId, String userId) throws Exception {
		Optional<Course> exist = courseRepository.findById(courseId);

		if (exist.isPresent()) {
			Course updated = exist.get();

			List<User> students = new ArrayList<>();
			boolean add = true;

			for (User u : updated.getStudents()) {
				if (!u.getId().equalsIgnoreCase(userId)) {
					students.add(u);
				} else {
					add = false;
				}
			}
			if (add) {
				students.add(userRepository.findById(userId).get());
			}

			updated.setStudents(students);
			courseRepository.save(updated);

			return convertEntityToDto(updated);
		} else {
			throw new Exception("No se puedo actualizar.");
		}

	}

	@Override
	@Transactional
	public CourseDto removeProfessor(Long id) throws Exception {

		Optional<Course> exist = courseRepository.findById(id);

		if (exist.isPresent()) {
			Course updated = exist.get();

			updated.setProfessor(null);
			courseRepository.save(updated);

			return convertEntityToDto(updated);
		} else {
			throw new Exception("No se puedo actualizar.");
		}
	}

}
