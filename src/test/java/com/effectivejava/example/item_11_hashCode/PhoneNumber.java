package com.effectivejava.example.item_11_hashCode;

import java.util.Objects;


/** 전형적인 hashCode 메서드
 * 1. 변수 result를 선언 후, 값 c로 초기화한다.
 *  1.1 여기서 값 c란, 해당 객체의 첫번째 핵심 필드를 2.1 방식으로 계산한 해시코드이다.
 *  1.2 핵심 필드란, equals 비교에 사용되는 필드를 말한다.
 * 2. 해당 객체의 나머지 핵심 필드 f 각각에 대해 다음 작업을 수행한다.
 *  2.1 해당 필드의 해시코드 c를 계산한다.
 *      - 기본 타입 필드라면 => Type.hashCode(f)를 수행 (Type 은 박싱 클래스)
 *      - 참조 타입 필드라면 => 필드의 hashCode를 호출한다.
 *      - 필드 값이 null이면 => 0을 사용한다.
 *      - 필드가 배열이면서, 일부가 핵심원소 => 핵심 원소 각각을 별도 필드처럼 다룬다. 즉, 각각 hashCode를 부르고, 2.2 방식으로 갱신한다.
 *      - 필드가 배열인데, 핵심 원소가 하나도 없다면 => 상수를 사용한다.
 *      - 필드가 배열이면서, 전부 핵심원소 => Arrays.hashCode
 *  2.2 2.1에서 계산한 해시코드 c를 result에 갱신시킨다. (result = 31 * result + c)
 *      - 곱할 숫자가 31인 이유는 홀수 이면서 소수이기 때문
 *          - 홀수: 숫자가 짝수이고 오버플로가 발생하면, 정보를 잃게 됨 (2를 곱하는 것은 시프트 연산과 같은 결과를 낸다고 함)
 *          - 소수는 전통적인 숫자.
 * */
class PhoneNumberNormal {
    private final short areaCode;
    private final short prefix;
    private final short lineNum;
    public PhoneNumberNormal(short areaCode, short prefix, short lineNum) {
        this.areaCode = areaCode;
        this.prefix = prefix;
        this.lineNum = lineNum;
    }
    @Override
    public int hashCode() {
        int result = Short.hashCode(areaCode);
        result = 31 * result + Short.hashCode(prefix);
        result = 31 * result + Short.hashCode(lineNum);
        return result;
    }
}

/**
 * Objects 클래스에서 hashCode를 가져와 사용할 수 있다. 하지만 더 느리다.
 * */
class PhoneNumberOneLine {
    private final short areaCode;
    private final short prefix;
    private final short lineNum;
    public PhoneNumberOneLine(short areaCode, short prefix, short lineNum) {
        this.areaCode = areaCode;
        this.prefix = prefix;
        this.lineNum = lineNum;
    }
    @Override
    public int hashCode() {
        return Objects.hash(areaCode, prefix, lineNum);
    }
}

/**
 * 해시코드를 지연 초기화 시킴
 * - 쓰레드 안정성까지 고려해야함
 * - 클래스가 불변이고, 해시 코드를 계산하는 비용이 크다면 캐싱하는 방식으로 사용됨
 * */
class PhoneNumberLazyInitialization {
    private final short areaCode;
    private final short prefix;
    private final short lineNum;
    public PhoneNumberLazyInitialization(short areaCode, short prefix, short lineNum) {
        this.areaCode = areaCode;
        this.prefix = prefix;
        this.lineNum = lineNum;
    }

    private int hashCode;
    @Override
    public int hashCode() {
        int result = hashCode;
        if(result == 0) {
            result = Short.hashCode(areaCode);
            result = 31 * result + Short.hashCode(prefix);
            result = 31 * result + Short.hashCode(lineNum);
            hashCode = result;
        }
        return result;
    }
}