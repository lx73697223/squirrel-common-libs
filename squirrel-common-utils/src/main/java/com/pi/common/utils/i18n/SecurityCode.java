package com.pi.common.utils.i18n;

public enum SecurityCode implements MessageCode {

    LOGIN_SUCCESS(101),
    INVALID_ACCOUNT(102),
    ACCOUNT_UNACTIVATED(103),
    ACCOUNT_ACTIVATED(104),
    ACCOUNT_DISABLED(105),
    ACCOUNT_EXPIRED(106),
    INCORRECT_CREDENTIALS(107),
    INCORRECT_CAPTCHA(108),
    PHONE_REGISTERED(109),
    REGISTRATION_SUCCESS(201),
    REGISTRATION_EMAIL_CHECK(202),
    REGISTRATION_CODE_INCORRECT(203),
    INVALID_SESSION(301),
    REQUIRE_PERMISSION(302);

    private int id;

    private SecurityCode(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }
}
