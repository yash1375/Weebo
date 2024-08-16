package data

import android.content.Context
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.datastore.dataStore
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import kotlinx.coroutines.CoroutineDispatcher

class ImageRepoImpl(private val dispatcher: CoroutineDispatcher,private val dao: ImageDao,val context: Context): ImageRepo {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }
    private lateinit var response : HttpResponse
    override suspend fun getPost(page:Int?,limit:Int,tag:String?): List<ImageNetwork> {
        if (tag == "") {
            response =
                client.get("https://danbooru.donmai.us/posts.json?page=${page}&limit=${limit}")
        }
        else{
            response =
                client.get("https://danbooru.donmai.us/posts.json?page=${page}&limit=${limit}&tags=$tag")
        }
        return response.body()
    }

    override suspend fun clear() {
        dao.clearAll()
    }
}