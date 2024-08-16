package data

interface ImageRepo {
    suspend fun getPost(page: Int? = 1,limit: Int,tag:String?=""): List<ImageNetwork>
    suspend fun clear()
}