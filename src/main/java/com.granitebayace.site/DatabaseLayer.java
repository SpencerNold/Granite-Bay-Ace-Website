package com.granitebayace.site;

import me.spencernold.kwaf.database.Driver;
import me.spencernold.kwaf.database.impl.SQLiteDatabase;
import me.spencernold.kwaf.services.Service;

@Service.Database(driver = Driver.Type.SQLITE, url = "jdbc:sqlite:dev_sqlite.db")
public class DatabaseLayer extends SQLiteDatabase {

}
