package ktor.test

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.intellij.lang.annotations.Language
import java.sql.ResultSet
import javax.sql.DataSource

suspend fun <T> DataSource.list(@Language("SQL") sql: String, block: (ResultSet) -> T): List<T> {
    return withContext(Dispatchers.IO) {
        connection.use { connection ->
            connection.prepareStatement(sql).use { preparedStatement ->
                preparedStatement.executeQuery().use { resultSet ->
                    val results = mutableListOf<T>()
                    while (resultSet.next()) results.add(block(resultSet))
                    results
                }
            }
        }
    }
}
