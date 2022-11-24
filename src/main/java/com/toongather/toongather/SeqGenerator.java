package com.toongather.toongather;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import javax.swing.plaf.nimbus.State;
import javax.xml.transform.Result;
import java.io.Serializable;
import java.sql.*;
import java.util.Properties;


/**
 * 사용자 정의 시퀀스를 반환합니다.
 */
public class SeqGenerator implements IdentifierGenerator {

    public static final String SEQ_NAME = "seqName";
    public static final String PREFIX = "prefix";

    private String seqName;
    private String prefix;

    /**
     * @GenericGenerator에서 넘어온 파라미터를 내부에 선언한 변수에 할당한다.
     *
     * @param type
     * @param params @GenericGenerator에서 넘어온 파라미터
     * @param serviceRegistry
     * @throws MappingException
     */
    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        IdentifierGenerator.super.configure(type, params, serviceRegistry);

        this.seqName = ConfigurationHelper.getString(SEQ_NAME,params);
        this.prefix = ConfigurationHelper.getString(PREFIX,params);
    }


    /**
     * 커스텀한 시퀀스를 반환한다.
     * @param session
     * @param object
     * @return newSeq 사용자 정의 시퀀스
     * @throws HibernateException
     */
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {

        String sql= null;
        String newSeq = "";

        sql = "SELECT NEXTVAL(" +this.seqName +")";
        // JDBC Connection
        Connection con = null;
        try {
            con = session.getJdbcConnectionAccess().obtainConnection();  // 공유세션으로부터 jdbc connection을 얻는다
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql); // SQL를 실행

            if(rs.next()) {
                newSeq = this.prefix +"-"+ rs.getString(1); // 결과값 추출
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

            return newSeq;

    }
}
