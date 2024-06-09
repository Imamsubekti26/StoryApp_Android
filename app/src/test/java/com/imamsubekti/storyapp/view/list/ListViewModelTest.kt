package com.imamsubekti.storyapp.view.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.imamsubekti.storyapp.entity.Story
import com.imamsubekti.storyapp.network.ApiService
import com.imamsubekti.storyapp.repository.DataStoreRepository
import com.imamsubekti.storyapp.repository.StoryRepository
import com.imamsubekti.storyapp.utils.NoopListUpdateCallback
import com.imamsubekti.storyapp.utils.StoryDiffCallback
import com.imamsubekti.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.*
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ListViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var dataStoreRepository: DataStoreRepository
    @Mock
    private lateinit var storyRepository: StoryRepository
    @Mock
    private lateinit var apiService: ApiService

    private lateinit var viewModel: ListViewModel

    @Before
    fun setUp() {
        viewModel = ListViewModel(dataStoreRepository, apiService, storyRepository)
    }

    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setupDispatcher() {
        Dispatchers.setMain(testDispatcher)
    }
    @After
    fun tearDownDispatcher() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when updateList is called and data is successfully loaded`() {
        val token = "token"
        val storyList = listOf(
            Story(id = "1", name = "Title 1", description = "Content 1"),
            Story(id = "2", name = "Title 2", description = "Content 2"),
            Story(id = "3", name = "Title 3", description = "Content 3"),
            Story(id = "4", name = "Title 4", description = "Content 4"),
            Story(id = "5", name = "Title 5", description = "Content 5"),
        )
        val pagingData = PagingData.from(storyList)
        val liveData = MutableLiveData(pagingData)

        `when`(storyRepository.getStory(apiService, "Bearer $token"))
            .thenReturn(liveData)

        viewModel.updateList("token")
        verify(storyRepository).getStory(apiService, "Bearer $token")

        val actuallyData = viewModel.listStory.getOrAwaitValue()
        Assert.assertNotNull(actuallyData)

        val actualList = getListFromPagingData(actuallyData)
        Assert.assertEquals(storyList.size, actualList.size)
        Assert.assertEquals(storyList[0], actualList[0])
    }

    @Test
    fun `when updateList is called but data is empty`() {
        val token = "token"
        val storyList = listOf<Story>()
        val pagingData = PagingData.from(storyList)
        val liveData = MutableLiveData(pagingData)

        `when`(storyRepository.getStory(apiService, "Bearer $token"))
            .thenReturn(liveData)

        viewModel.updateList("token")
        verify(storyRepository).getStory(apiService, "Bearer $token")

        val actuallyData = viewModel.listStory.getOrAwaitValue()
        val actualList = getListFromPagingData(actuallyData)
        Assert.assertEquals(0, actualList.size)
    }

    private fun getListFromPagingData(pagingData: PagingData<Story>): List<Story> {
        val items = mutableListOf<Story>()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryDiffCallback(),
            updateCallback = NoopListUpdateCallback(),
            mainDispatcher = Dispatchers.Main,
            workerDispatcher = Dispatchers.Default
        )
        runBlocking {
            differ.submitData(pagingData)
        }
        items.addAll(differ.snapshot().items)
        return items
    }
}
