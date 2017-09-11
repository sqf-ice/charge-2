package com.clouyun.charge.common.secruity;

import com.clouyun.boot.common.domain.DataVo;
import com.clouyun.boot.common.utils.CalendarUtils;
import com.clouyun.boot.common.utils.DateUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;

    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    private static final String CLAIM_KEY_ID = "jti";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    // token参数名
    @Value("${jwt.header}")
    private String tokenHeader;

    /**
     * 根据token信息获取用户ID
     * @param token
     * @return
     */
    public Integer getUserIdFromToken(String token) {
        Integer userId = null;
        try {
            final Claims claims = getClaimsFromToken(token);
            userId = Integer.valueOf(claims.getId());
        } catch (Exception e) {
        }
        return userId;
    }

    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    public Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    public String generateToken(JwtUser userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        claims.put(CLAIM_KEY_ID, userDetails.getId());
        return generateToken(claims);
    }

    String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String generateToken(DataVo claims) {
        claims.put(CLAIM_KEY_USERNAME, claims.getString("loginName"));
        claims.put(CLAIM_KEY_CREATED, new Date());
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && !isTokenExpired(token);
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public Boolean validateToken(String userName, String token, JwtUser user) {
        //JwtUser user = (JwtUser) userDetails;
        //final String username = getUsernameFromToken(token);
        final Claims claims = getClaimsFromToken(token);
        final Date created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        final Date expiration = claims.getExpiration();

        return (
                userName.equals(user.getUsername())
                        && !expiration.before(new Date())
                        && !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate()));
    }

    public static void main(String[] args) {
        //System.out.println(DateUtils.formatDateTime(new Date(1497529532*1000L)));
        //System.out.println(1497529532*1000L);
        //System.out.println(System.currentTimeMillis()+30*1000);
        //Date date = new Date();
        //System.out.println(DateUtils.formatDateTime(date));
        //Long l = date.getTime()/1000;
        //System.out.println(DateUtils.formatDateTime(new Date(((Long)1497529532L).longValue())));

        System.out.println(DateUtils.truncatedCompareTo(new Date(System.currentTimeMillis()+60*1000),new Date(), Calendar.MINUTE));
        System.out.println(CalendarUtils.compareTime(Calendar.getInstance(),CalendarUtils.getCalendar(new Date(System.currentTimeMillis()+300*1000)), Calendar.MINUTE));
        Map map = new HashMap();
        map.put("create",new Date());
        System.out.println(DateUtils.formatDateTime((Date)map.get("create")));
    }
}

