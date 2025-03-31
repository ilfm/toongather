package com.toongather.toongather.global.security.oauth2;

import org.springframework.stereotype.Component;

@Component
public class OAuth2Provider {

  private OAuth2Provider() {
  }

  public static class Google {

    private Google() {}

    public static final String PROVIDER_NAME = "google";
    public static final String SCOPE_EMAIL = AttributeName.EMAIL;
    public static final String SCOPE_NAME = AttributeName.NAME;
    public static final String SCOPE_NICKNAME = "given_name";
  }

  public static class Naver {

    private Naver() {}

    public static final String PROVIDER_NAME = "naver";
    public static final String SCOPE_NAME = AttributeName.NAME;
    public static final String SCOPE_EMAIL = AttributeName.EMAIL;
    public static final String SCOPE_NICKNAME = AttributeName.NICKNAME;
    public static final String WRAPPER = "response";
  }

  public static class Kakao {

    private Kakao() {}

    public static final String PROVIDER_NAME = "kakao";
    public static final String SCOPE_NAME = AttributeName.NAME;
    public static final String SCOPE_EMAIL = AttributeName.EMAIL;// (v2) 프로필 정보 접근 권한
    public static final String SCOPE_NICKNAME = AttributeName.NICKNAME;
    public static final String PROFILE = "profile";
    public static final String WRAPPER = "kakao_account";
  }

  public static class AttributeName {

    private AttributeName() {}

    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String NICKNAME = "nickname";
  }


}
