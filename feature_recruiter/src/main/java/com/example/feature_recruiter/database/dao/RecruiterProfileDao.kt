//package com.example.feature_recruiter.database.dao
//
//import androidx.room.*
//import com.example.feature_recruiter.database.entity.RecruiterProfileEntity
//import kotlinx.coroutines.flow.Flow
//
//@Dao
//interface RecruiterProfileDao {
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertProfile(profile: RecruiterProfileEntity)
//
//    @Query("SELECT * FROM recruiter_profile WHERE email = :email")
//    suspend fun getProfileByEmail(email: String): RecruiterProfileEntity?
//
//    @Query("SELECT * FROM recruiter_profile WHERE email = :email")
//    fun getProfileFlow(email: String): Flow<RecruiterProfileEntity?>
//
//    @Update
//    suspend fun updateProfile(profile: RecruiterProfileEntity)
//
//    @Query("UPDATE recruiter_profile SET setupCompleted = 1 WHERE email = :email")
//    suspend fun markSetupCompleted(email: String)
//
//    @Query("UPDATE recruiter_profile SET companyName = :companyName, recruiterName = :recruiterName WHERE email = :email")
//    suspend fun updateCompanyInfo(
//        email: String,
//        companyName: String,
//        recruiterName: String
//    )
//
//    @Delete
//    suspend fun deleteProfile(profile: RecruiterProfileEntity)
//}


package com.example.feature_recruiter.database.dao

import androidx.room.*
import com.example.feature_recruiter.database.entity.RecruiterProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecruiterProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: RecruiterProfileEntity)

    @Query("SELECT * FROM recruiter_profile WHERE email = :email")
    suspend fun getProfileByEmail(email: String): RecruiterProfileEntity?

    @Query("SELECT * FROM recruiter_profile WHERE email = :email")
    fun getProfileFlow(email: String): Flow<RecruiterProfileEntity?>

    @Update
    suspend fun updateProfile(profile: RecruiterProfileEntity)

    // ✅ Check if profile exists and setup completed
    @Query("SELECT setupCompleted FROM recruiter_profile WHERE email = :email LIMIT 1")
    suspend fun isSetupCompleted(email: String): Boolean?

    // ✅ Mark setup completed
    @Query("UPDATE recruiter_profile SET setupCompleted = 1 WHERE email = :email")
    suspend fun markSetupCompleted(email: String)

    @Query("UPDATE recruiter_profile SET companyName = :companyName, recruiterName = :recruiterName, setupCompleted = 1 WHERE email = :email")
    suspend fun updateCompanyInfoAndMarkSetup(
        email: String,
        companyName: String,
        recruiterName: String
    )
    @Query("SELECT * FROM recruiter_profile WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): RecruiterProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: RecruiterProfileEntity)
    @Delete
    suspend fun deleteProfile(profile: RecruiterProfileEntity)
}


