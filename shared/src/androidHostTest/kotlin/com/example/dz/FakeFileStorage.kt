package com.example.dz

import com.example.dz.data.local.file.FileStorage

/** In-memory [FileStorage] keyed by a synthetic path. */
internal class FakeFileStorage : FileStorage {
    val files = mutableMapOf<String, String>()
    override fun save(bookId: String, text: String): String {
        val path = "/mem/$bookId.txt"
        files[path] = text
        return path
    }
    override fun read(path: String): String? = files[path]
    override fun delete(path: String): Boolean {
        files.remove(path)
        return true
    }
    override fun exists(path: String): Boolean = files.containsKey(path)
    override fun deleteAll(): Boolean {
        files.clear()
        return true
    }
}
