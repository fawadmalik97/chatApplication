package com.chatapplication.Services;

import com.chatapplication.Model.entity.Chat;
import com.chatapplication.Model.Interface.UserChatsAndCategories;
import com.chatapplication.Model.entity.User;
import com.chatapplication.Repository.ChatRepository;
import com.chatapplication.Repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Fawad khan Created Date : 08-October-2021 A service class of user
 *         connected with repository which contains user CRUD operations
 */
@Service
public class UserService {
	final private UserRepository userRepository;
	final private ChatRepository chatRepository;
	final private UserChatsAndCategories userChatsAndCategories;

	private static final Logger log = LogManager.getLogger(UserService.class);

	// Autowiring through constructor
	public UserService(UserRepository userRepository, ChatRepository chatRepository, UserChatsAndCategories userChatsAndCategories) {
		this.chatRepository = chatRepository;
		this.userRepository = userRepository;
		this.userChatsAndCategories = userChatsAndCategories;
	}

	/**
	 * @author Fawad khan
	 * @return List of users
	 */
	// Get list of all users
	public ResponseEntity<Object> listAllUser() {
		try {
			List<User> users = userRepository.findAll();
			log.info("list of users fetch from db are ", users);
			// check if database is empty
			if (users.isEmpty()) {
				return new ResponseEntity<>("Message:  Users are empty", HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<>(users, HttpStatus.OK);
			}

		} catch (Exception e) {
			log.error(
					"some error has occurred trying to Fetch users, in Class  UserService and its function listAllUser ",
					e.getMessage());
			return new ResponseEntity<>("User could not be found", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 *
	 * @param id
	 * @return
	 */
	// get user by specific id
	public ResponseEntity<Object> getUserById(Long id) {
		try {
			Optional<User> user = userRepository.findById(id);
			if (user.isPresent())
				return new ResponseEntity<>(user, HttpStatus.FOUND);
			else
				return new ResponseEntity<>("could not found user with given details....", HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error(
					"some error has occurred during fetching User by id , in class UserService and its function getUserById ",
					e.getMessage());

			return new ResponseEntity<>("Unable to find User, an error has occurred", HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	public ResponseEntity<Object> getUserChats(Long id) {
		try {
			Optional<User> user = userRepository.findById(id);
			if (user.isPresent())

				return new ResponseEntity<>(user, HttpStatus.FOUND);
			else
				return new ResponseEntity<>("could not found user with given details....", HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error(
					"some error has occurred during fetching User by id , in class UserService and its function getUserById ",
					e.getMessage());

			return new ResponseEntity<>("Unable to find User, an error has occurred", HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	/**
	 *
	 * @param userid
	 * @param chats
	 * @return
	 */
	// add chats for specific user
	public ResponseEntity<Object> addChatsInUser(Long userid, List<Chat> chats) {
		try {
			// find user in db and store in User object
			Optional<User> user = userRepository.findById(userid);
			if (user.isPresent()) {

				// get all chats from user
				List<Chat> userChats = user.get().getChats();

				// add created date to chats\
				for (Chat chat : chats) {
					String pattern = "dd-M-yyyy hh:mm:ss";
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
					String date = simpleDateFormat.format(new Date());
					chat.setCreatedDate(date);
				}

				// add new chats to existing chatlist
				userChats.addAll(chats);
				String userResult = user.get().toString();

				// save updated user object to chats
				userRepository.save(user.get());
				System.out.println(userResult + " ");
				return new ResponseEntity<>(userChats, HttpStatus.OK);
			} else
				return new ResponseEntity<>("Could not find user please correct userid", HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error(
					"some error has occurred trying to save user,  Class  UserService and its function addChatsInUser ",
					e.getMessage());
			return new ResponseEntity<>("chats could not be added , Data maybe incorrect",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 *
	 * @param userName
	 * @param password
	 * @return
	 */
	public ResponseEntity<Object> getUserByNameAndPassword(String userName, String password) {
		try {
			Optional<User> user = userRepository.findByUserNameAndPassword(userName, password);
			if (user.isPresent())
				return new ResponseEntity<>("login success", HttpStatus.OK);
			else
				return new ResponseEntity<>("incorrect login details, Login failed", HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error(
					"some error has occurred during fetching User by username , in class UserService and its function getUserByName ",
					e.getMessage());

			return new ResponseEntity<>("Unable to Login either password or username might be incorrect",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 *
	 * @param user
	 * @return
	 */
	public ResponseEntity<Object> saveUser(User user) {
		try {
			String pattern = "dd-MM-yyyy hh:mm:ss";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String date = simpleDateFormat.format(new Date());
			user.setCreatedDate(date);
			userRepository.save(user);
			user.toString();
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity<>("Data already exists .. duplicates not allowed ", HttpStatus.CONFLICT);
		}

		catch (Exception e) {
			log.error(
					"some error has occurred while trying to save user,, in class ChatService and its function saveUser ",
					e.getMessage());
			return new ResponseEntity<>("Chats could not be added , Data maybe incorrect",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 *
	 * @param user
	 * @return
	 */
	public ResponseEntity<Object> updateUser(User user) {
		try {
			String pattern = "dd-MM-yyyy hh:mm:ss";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String date = simpleDateFormat.format(new Date());
			user.setUpdatedDate(date);
			userRepository.save(user);
			user.toString();
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			log.error(
					"some error has occurred while trying to update user,, in class ChatService and its function updateUser ",
					e.getMessage());
			return new ResponseEntity<>("Chats could not be added , Data maybe incorrect",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	public ResponseEntity<Object> deleteUser(Long id) {
		try {
			userRepository.deleteById(id);
			return new ResponseEntity<>("Message: User deleted successfully", HttpStatus.OK);
		} catch (DataAccessException e) {
			return new ResponseEntity<>("Message: User does not exists ", HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			log.error(
					"some error has occurred while trying to Delete user,, in class UserService and its function deleteUser ",
					e.getMessage(), e.getCause(), e);
			return new ResponseEntity<>("User could not be Deleted.......", HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

}
