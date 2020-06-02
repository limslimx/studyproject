package com.studyproject.book;

import com.studyproject.domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Repository
public class RedisBookRepository {

    @Autowired
    private RedisTemplate redisTemplate;

    //redis에 해당 키가 존재하는지 확인
    public boolean getExists(String key) throws Exception {
        return redisTemplate.hasKey(key);
    }

    //redis에서 키에 해당하는 도서 정보 리스트 형태로 가져옴
    public List<Book> getBookInfoFromRedis(String key) {
        List<Book> bookList = null;
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Book.class));

        if (redisTemplate.hasKey(key)) {
            bookList = (List) redisTemplate.opsForList().range(key, 0, -1);
        }
        return bookList;
    }

    //redis에 해당 키로 도서 정보 리스트 형태로 저장
    public void setBookInfoInRedis(String key, List<Book> bookList) throws Exception {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Book.class));

        if (this.getExists(key)) {
            redisTemplate.delete(key);
        }

        Iterator<Book> it = bookList.iterator();
        while (it.hasNext()) {
            Book book = (Book) it.next();
            redisTemplate.opsForList().rightPush(key, book);
            book = null;
        }
    }

    //ttl 설정
    public void setTimeOutHour(String roomKey, int hours) {
        redisTemplate.expire(roomKey, hours, TimeUnit.HOURS);

    }
}
