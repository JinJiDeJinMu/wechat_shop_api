package com.chundengtai.base.shiro;

import com.chundengtai.base.utils.Constant;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 分布式session管理
 */
@Component
public class CluterShiroSessionDao extends EnterpriseCacheSessionDAO {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = super.doCreate(session);

        final String key = Constant.SESSION_KEY + sessionId.toString();
        setShiroSession(key, session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        Session session = super.doReadSession(sessionId);
        if (null == session) {
            final String key = Constant.SESSION_KEY + sessionId.toString();
            session = getShiroSession(key);
        }
        return session;
    }

    @Override
    protected void doUpdate(Session session) {
        super.doUpdate(session);
        final String key = Constant.SESSION_KEY + session.getId().toString();
        setShiroSession(key, session);
    }

    @Override
    protected void doDelete(Session session) {
        super.doDelete(session);
        final String key = Constant.SESSION_KEY + session.getId().toString();
        redisTemplate.delete(key);
    }

    private Session getShiroSession(String key) {
        return (Session) redisTemplate.opsForValue().get(key);
    }

    private void setShiroSession(String key, Session session) {
        redisTemplate.opsForValue().set(key, session, 1, TimeUnit.DAYS);
    }
}
