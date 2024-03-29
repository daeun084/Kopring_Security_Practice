package com.example.Kopring.global.config

import com.example.Kopring.domain.member.entity.Member
import com.example.Kopring.domain.member.repository.MemberRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService (
        private val memberRepository: MemberRepository,
        private val passwordEncoder: PasswordEncoder
): UserDetailsService{
    override fun loadUserByUsername(username: String): UserDetails =
        memberRepository.findByLoginId(username) //member가 존재하면 userDetails 반환
                ?.let { createUserDetails(it) } ?: throw UsernameNotFoundException("존재하지 않는 유저입니다")

    private fun createUserDetails(member: Member): UserDetails =
            CustomUser(
                    member.id!!, //custom User 사용해 member id 같이 저장
                    member.loginId,
                    passwordEncoder.encode(member.password),
                    member.memberRole!!.map { SimpleGrantedAuthority("ROLE_${it.role}") }
            )

}