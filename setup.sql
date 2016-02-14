BEGIN
FOR c IN (SELECT TABLE_NAME FROM user_tables) loop
EXECUTE immediate ('drop table '||c.TABLE_NAME||' cascade constraints');
END loop;
END;
/
BEGIN
FOR c IN (SELECT * FROM user_objects) loop
EXECUTE immediate ('drop '||c.object_type||' '||c.object_name);
END loop;
END;
/
 
CREATE TABLE Temperature (
        time        int(11),
        value        float(50) NOT NULL,
        PRIMARY KEY (time)
);

CREATE TABLE Audio (
        time        int(11),
        value         float(50) NOT NULL,
        PRIMARY KEY (time)
);

CREATE TABLE Light (
        time        int(11),
        value         float(50) NOT NULL,
        PRIMARY KEY (time)
);

CREATE TABLE CO2 (
        time        int(11),
        value         float(50) NOT NULL,
        PRIMARY KEY (time)
);

CREATE TABLE Motion (
        time        int(11),
        value        float(50) NOT NULL,
        value2     float(50) NOT NULL,
        value3    float(50) NOT NULL,
        PRIMARY KEY (time)
);

 
