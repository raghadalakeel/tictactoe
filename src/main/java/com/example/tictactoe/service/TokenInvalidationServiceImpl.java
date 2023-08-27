package com.example.tictactoe.service;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TokenInvalidationServiceImpl implements TokenInvalidationService {

    private Set<String> invalidatedTokens = new HashSet<>();

    @Override
    public void invalidateToken(String token) {
        invalidatedTokens.add(token);
    }

    public boolean isTokenInvalidated(String token) {
        return invalidatedTokens.contains(token);
    }
}
