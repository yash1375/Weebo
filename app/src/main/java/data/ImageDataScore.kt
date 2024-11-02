package data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable

class ImageDataScore(
    val context: Context
) {
    companion object {
        private val Context.dataStore : DataStore<Preferences> by preferencesDataStore("unblur")
        val UNBLUR_key = booleanPreferencesKey("unblur")
        val NSFW_KEY = booleanPreferencesKey("nsfw")
        val FIRST_RUN = booleanPreferencesKey("first_run")
        val GRID_NUMBER = intPreferencesKey("grid_number")
        val GRID_TYPE = booleanPreferencesKey("grid_type")
    }
    val getNsfw : Flow<Boolean?> = context.dataStore.data.map {
        it[NSFW_KEY] ?: false
    }
    val gridNumber : Flow<Int?> = context.dataStore.data.map {
        it[GRID_NUMBER] ?: 2
    }
    val gridType : Flow<Boolean?> = context.dataStore.data.map {
        it[GRID_TYPE] ?: true
    }
    val getFirstRun: Flow<Boolean?> = context.dataStore.data.map {
        it[FIRST_RUN] ?:false
    }
    val getUnblur : Flow<Boolean?> = context.dataStore.data.map {
        it[UNBLUR_key] ?: false
    }
    suspend fun gridTypeChange(boolean: Boolean){
        context.dataStore.edit {
            it[GRID_TYPE] = boolean
        }
    }
    suspend fun gridNumberChange(data: Int){
        context.dataStore.edit {
            it[GRID_NUMBER] = data
        }
    }
    suspend fun NsfwToggle(boolean: Boolean){
        context.dataStore.edit {
            it[NSFW_KEY] = boolean
        }
    }
    suspend fun UnblurToggle(boolean: Boolean){
        context.dataStore.edit {
            it[UNBLUR_key] = boolean
        }
    }
    suspend fun FirstRunComplete(boolean: Boolean){
        context.dataStore.edit {
            it[FIRST_RUN] = boolean
        }
    }
}