package com.toongather.toongather;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import javax.xml.transform.Result;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class SeqGenerator implements IdentifierGenerator {

    public static final String METHOD = "method";
    public static final String PREFIX = "prefix";

    private String method;
    private String prefix;
    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        IdentifierGenerator.super.configure(type, params, serviceRegistry);

        this.method = ConfigurationHelper.getString(METHOD,params);
        this.prefix = ConfigurationHelper.getString(PREFIX,params);
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        String sql= null;
        String newId= "";
        switch (prefix){
            case "WTPF":
                sql = "SELECT WEBTOON_PLATFORM_SEQ.NEXTVAL FROM DUAL";
                break;
            default:
                break;
        }
        // JDBC Connection
        Connection con = null;
        try {
            con = session.getJdbcConnectionAccess().obtainConnection();  // 공유세션으로부터 jdbc connection을 얻는다
            CallableStatement callStatement = con.prepareCall(sql);
            callStatement.executeQuery(); // SQL를 실행
            ResultSet rs = callStatement.getResultSet();

            if(rs.next()) {
                newId = this.prefix +"-"+ rs.getString(1); // 결과값 추출
            }
        } catch (SQLException sqlException) {
            throw new HibernateException(sqlException);
        } finally {
            try {
                if(!con.isClosed()) {
                    con.close();
                }
            }catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return newId;

    }
}
