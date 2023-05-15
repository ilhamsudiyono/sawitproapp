package com.miniproject.interviewcode.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.miniproject.interviewcode.model.user.User;



@Repository
public interface IUserRepository extends JpaRepository<User, Long>{
	
	@Query(value = "SELECT * FROM m_user WHERE id = :idUser", nativeQuery = true)
	public List<User> getListByIdUser(@Param("idUser") Long userId);
	
	@Query(value = "SELECT * FROM m_user WHERE nama = :nama_", nativeQuery = true)
	public User getListByNama(@Param("nama_") String email);
	
	@Query(value = "SELECT * FROM m_user WHERE no_telp = :noTelp_", nativeQuery = true)
	public List<User> getListByNoHp(@Param("noTelp_") String noTlp);
	
	 
	Optional<User> findByNama(String nama);
	
	Optional<User> findByNoTelp(String noTlp);
	

	
//	@Query(value="select id, no_telp, email, password, is_login from m_user where no_telp = :notelp or email = :eml", nativeQuery = true)
//	Optional<User> findByNoTelpOrEmail(String notelp,  String eml);
	
	Boolean existsByNama(String nama);
	
	Boolean existsByNoTelp(String noTelp);
	
	@Query(value = "select u.nama from m_user u where u.id ?1", nativeQuery = true)
	List<String> findNamaByIdList(@Param("id") List<Long> idUser);

//	@Query(value = "SELECT * FROM m_user WHERE u.id ?1", nativeQuery = true)
//	public Optional<User> findById(String string);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE m_user SET is_login = true WHERE nama = :nama", nativeQuery = true)
	public int updateUserLoginByNama(@Param("nama") String nama);
	    
	public Optional<User> findByNoTelpAndPassword(String noTlp,  String pass);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE m_user SET is_login = true WHERE no_telp = :noTelp", nativeQuery = true)
	public int updateUserLoginByNoTelp(@Param("noTelp") String no_telp);
	
	
	@Query(value="select id, no_telp, nama, password, is_login, created_at, updated_at from m_user where no_telp = :notelp or nama = :nm", nativeQuery = true)
	public List<User> getNoTelpOrNama(@Param("notelp") String _notelp, @Param("nm")  String _nm);
	
	@Query(value = "SELECT * FROM m_user WHERE no_telp =:notelp ORDER BY id", nativeQuery = true)
	public List<User> getAllByNoTelp(@Param("notelp") String tlp);

	@Transactional
    @Modifying 
    @Query(value = "DELETE FROM m_user WHERE no_telp = :noTlp",nativeQuery = true) // if want to write nativequery then mask nativeQuery  as true
    int deleteByNoTlp(@Param("noTlp") String tlp); 
    
    @Transactional
    @Modifying 
    @Query(value = "DELETE FROM r_user_role WHERE user_id = :id_",nativeQuery = true) // if want to write nativequery then mask nativeQuery  as true
    int deleteByRuser(@Param("id_") Long _id); 
    	
	
}
