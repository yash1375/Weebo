package data

interface Downloader {
    fun downloadfile(url : String,name : String,fileType:String) : Long
}
