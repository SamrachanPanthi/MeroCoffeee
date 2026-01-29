package com.example.merocofeee.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.merocofeee.model.UserModel
import com.example.merocofeee.repository.UserRepo
import org.junit.*
import org.junit.Assert.*
import org.mockito.Mockito.*
import org.mockito.kotlin.any

class UserViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var repo: UserRepo
    private lateinit var viewModel: UserViewModel

    @Before
    fun setup() {
        repo = mock(UserRepo::class.java)
        viewModel = UserViewModel(repo)
    }

    // ===============================
    // Test 1: getUserById()
    // ===============================
    @Test
    fun getUserById_success_updatesUserLiveData() {
        val fakeUser = UserModel(
            userId = "1",
            email = "test@gmail.com"
        )

        doAnswer {
            val callback = it.arguments[1] as (Boolean, String, UserModel?) -> Unit
            callback(true, "Success", fakeUser)
            null
        }.`when`(repo).getUserById(eq("1"), any())

        viewModel.getUserById("1")

        assertEquals("test@gmail.com", viewModel.users.value?.email)
    }

    // ===============================
    // Test 2: getAllUser()
    // ===============================
    @Test
    fun getAllUser_success_updatesAllUsersAndLoading() {
        val fakeList = listOf(
            UserModel("1", "a@gmail.com"),
            UserModel("2", "b@gmail.com")
        )

        doAnswer {
            val callback = it.arguments[0] as (Boolean, String, List<UserModel?>?) -> Unit
            callback(true, "Success", fakeList)
            null
        }.`when`(repo).getAllUser(any())

        viewModel.getAllUser { _, _, _ -> }

        assertEquals(2, viewModel.allUsers.value?.size)
        assertEquals(false, viewModel.loading.value)
    }

    // ===============================
    // Test 3: logout()
    // ===============================
    @Test
    fun logout_callsRepo() {
        viewModel.logout { _, _ -> }
        verify(repo).logout(any())
    }
}
