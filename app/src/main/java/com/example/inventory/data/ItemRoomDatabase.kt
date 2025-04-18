/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.inventory.data
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.inventory.DateTypeConverter

/**
 * Database class with a singleton INSTANCE object.
 */

//previous DB version = "1"
//current DB version = "2"
//updated date = "15/03/2024
@Database(entities = [Item::class, SettingData::class, ExpenseData::class, IncomeData::class, SubExpenseData::class, SubIncomeData::class, TransactionListData::class], version = 1, exportSchema = false)
@TypeConverters(DateTypeConverter::class)
abstract class ItemRoomDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var INSTANCE: ItemRoomDatabase? = null

        fun getDatabase(context: Context): ItemRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ItemRoomDatabase::class.java,
                    "item_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
//                    .addMigrations(MIGRATION_V2)
                    .build()

                INSTANCE = instance
                // return instance
                instance
            }
        }

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Your migration logic goes here
                database.execSQL("ALTER TABLE T_Settings ADD COLUMN DT_CREATE INTEGER NOT NULL DEFAULT 0")
//                database.execSQL("CREATE TABLE IF NOT EXISTS T_Settings(ID TEXT PRIMARY KEY NOT NULL, DEFAULT_ITEMS TEXT NOT NULL)")
//                database.execSQL("CREATE TABLE IF NOT EXISTS ExpenseData (ID TEXT PRIMARY KEY NOT NULL, IMG_RESOURCE_ID INTEGER, " +
//                        "TYPE TEXT, DSCP_01 TEXT, DSCP_02 TEXT, DT_CREATE INTEGER, DT_UPDATE INTEGER)")
            }
        }
        private val MIGRATION_2_3: Migration = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Your migration logic goes here
                database.execSQL("ALTER TABLE T_Settings ADD COLUMN DT_UPDATE INTEGER NOT NULL DEFAULT 0")
            }
        }

        private val MIGRATION_V2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Your migration logic goes here
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS Expense (" +
                            "ID TEXT PRIMARY KEY NOT NULL," +
                            "Expense_ID TEXT NOT NULL," +
                            "IMG_RESOURCE_ID INTEGER NOT NULL," +
                            "TYPE TEXT NOT NULL," +
                            "DSCP_01 TEXT NOT NULL," +
                            "DSCP_02 TEXT," +
                            "DT_CREATE INTEGER," +
                            "DT_UPDATE INTEGER)"
                )
            }
        }
    }
}