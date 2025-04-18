package com.energy.tajo.user.domain;

import com.energy.tajo.global.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;


@Entity
@Getter
@Setter
@Table(name = "user")
public class User extends BaseTimeEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "uuid", nullable = false, columnDefinition = "CHAR(50)")
    private String uuid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "CHAR(64)")
    private String pw;

    @Column(nullable = false, length = 64)
    private String email;

    @Column(nullable = false, name = "phone_num")
    private String phoneNum;

    @Comment("True-삭제, False-삭제 아님")
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isDeleted;

    @Column(nullable = false)
    private boolean consentStatus;

    @Column(nullable = false)
    private boolean locationAgreed;

    @Comment("DB - default 값 0")
    @Column(nullable = false)
    private int points = 0;

    @Comment("DB - default 값 0")
    @Column(nullable = false, name = "tot_power_gen")
    private int totPowerGen = 0;

    @Column(name="refresh_token")
    private String refreshToken;

    protected User() {
    }

    private User(final String name, final String pw, final String email, final String uuid, final Boolean consentStatus) {
        this.name = name;
        this.pw = pw;
        this.email = email;
        this.uuid = uuid;
        this.isDeleted = false;
        this.consentStatus = consentStatus;
    }

    public static User of(final String uuid, final String pw, final String name, final String email, final Boolean consentStatus) {
        return new User(name, pw, email, uuid, consentStatus);
    }
}