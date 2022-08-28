package com.example.hellospring.repository;

import com.example.hellospring.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemoryMemberRepository implements MemberRepository {

    private static Map<Long, Member> store = new HashMap<>(); // 실제로는 concurrent hash-map 사용
    private static long sequence = 0L; // 실제로는 동시성 문제를 고려하여 atomic long 사용

    @Override
    public Member save(Member member) {
        member.setId(++sequence); // member의 id값 설정
        store.put(member.getId(), member); // id와 member를 store(해쉬맵)에 저장
        System.out.println("save : " + member.getName());
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id)); // id가 없는 경우 null 반환이 되므로, Optional의 ofNullable을 사용하여 감싸서 반환
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny();
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
