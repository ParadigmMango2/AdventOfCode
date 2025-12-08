package com.paradigmmango.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// NOTES:
// 1. does not work on concurrent calls
// 2. uses infinite space for caching - not always ideal. Day 7 alone took over 3000 entries
// 3. in the rare cases where there are overloaded methods with the same erasure signature - which java does a lot to prevent you from doing - the cache will be the same for both methods.
@Aspect
public class CacheAspect {
    public static final Map<String, Map<Object, Object>> caches = new ConcurrentHashMap<>();

    @Around("@annotation(cache) && args(.., *)")
    public Object cache(ProceedingJoinPoint pjp, Cache cache) throws Throwable {
        String signature = pjp.getSignature().toLongString();
        Map<Object, Object> signatureCache = caches.computeIfAbsent(signature, s -> new HashMap<>());

        Object key = buildKey(pjp.getArgs(), cache.cacheByParams());

        if (signatureCache.containsKey(key)) {
            return signatureCache.get(key);
        } else {
            Object result;

            try {
                result = pjp.proceed();
            } catch (Throwable throwable) {
                throw throwable;
            }

            signatureCache.put(key, result);

            return result;
        }
    }

    private List<Object> buildKey(Object[] args, int[] indices) {
        if (indices.length == 0) {
            return Arrays.asList(args);
        } else {
            List<Object> selectedArgs = new ArrayList<>();
            for (int index : indices) {
                selectedArgs.add(args[index]);
            }

            return selectedArgs;
        }
    }
}
