package com.example.ben.data.model.room.agent

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AgentDataDao {
    @Insert
    suspend fun insertAgent(agentData: AgentData) : Long
    @Update
    suspend fun updateAgent(agentData: AgentData) : Int
    @Query("SELECT * FROM agentdata WHERE id = :id")
    suspend fun getAgentById(id: Int): AgentData?
    @Query("SELECT * FROM agentdata WHERE account = :account")
    fun getAgentByAccount(account: String): LiveData<List<AgentData>>
    @Query("SELECT * FROM agentdata WHERE account = :account")
    suspend fun getAgentsByAccount(account: String): List<AgentData>?
}