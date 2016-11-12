package cn.edu.ustc.igank.database.table;

/**
 * Created by Lehman on 2016-06-25.
 */
public class GirlTable {
    public static final String NAME = "girl_table";

    public static final String ID = "_id";
    public static final String CREATED_AT = "createdAt";
    public static final String DESC = "desc";
    public static final String PUBLISHED_AT = "publishedAt";
    public static final String SOURCE = "source";
    public static final String TYPE = "type";
    public static final String URL = "url";
    public static final String USED = "used";
    public static final String WHO = "who";

    public static final int ID_ID = 0;
    public static final int ID_CREATED_AT = 1;
    public static final int ID_DESC = 2;
    public static final int ID_PUBLISHED_AT = 3;
    public static final int ID_SOURCE = 4;
    public static final int ID_TYPE = 5;
    public static final int ID_URL = 6;
    public static final int ID_USED = 7;
    public static final int ID_WHO = 8;



    public static final String CREATE_TABLE = "create table "+NAME+
            "("+ID+" text primary key,"+
            CREATED_AT+" text,"+
            DESC+" text,"+
            PUBLISHED_AT+" text," +
            SOURCE+" text," +
            TYPE+" text," +
            URL+" text," +
            USED+" INTEGER," +
            WHO+" text)";
}
