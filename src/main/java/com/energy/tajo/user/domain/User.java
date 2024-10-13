package com.energy.tajo.user.domain;

import com.energy.tajo.global.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;


@Entity
@Getter
@Setter
@Table(name = "user")
public class User extends BaseTimeEntity {

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
    @Column(nullable = false)
    private float totPowerGen = 0;

    @Comment("카드 정보 = Null 값 (카드 등록 시 입력되기 때문)")
    @Column(nullable = true, length = 16)
    private String cardNum;

    @Column(nullable = true, length = 5)
    private String validThru;

    @Column(nullable = true, length = 3)
    private String cvc;

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