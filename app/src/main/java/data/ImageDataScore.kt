package data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ImageDataScore(
    val context: Context
) {
    companion object {
        private val Context.dataStore : DataStore<Preferences> by preferencesDataStore("nsfw")
        val NSFW_KEY = booleanPreferencesKey("nsfw")
    }
    val getNsfw : Flow<Boolean?> = context.dataStore.data.map {
        it[NSFW_KEY] ?: false
    }

    suspend fun NsfwToggle(boolean: Boolean){
        context.dataStore.edit {
            it[NSFW_KEY] = boolean
        }
    }
}