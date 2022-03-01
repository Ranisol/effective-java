package com.effectivejava.example.item_10_equals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


class TestPoint {
    /**
     * 대칭성 위배
     * - Point 객체와 ColorPoint 객체를 바꿔서 비교하면 맞지 않음
     * - Point 객체의 equals는 색상을 무시하는 반면, ColorPoint는 색상까지 같은지 비교하기 때문
     * */
    @Test
    public void testViolateSymmetry() {
        Point x = new Point(1, 2);
        ColorPointViolationSymmetry y = new ColorPointViolationSymmetry(1, 2, Color.RED);
        Assertions.assertAll(
                () -> Assertions.assertTrue(x.equals(y)),
                () -> Assertions.assertFalse(y.equals(x))
        );
    }

    /**
     * 추이성 위배
     * - x와 z를 비교할때는 색상까지 비교하게 됨
     * - 더욱이 Point를 확장하는 새로운 SmellPoint를 만들고난뒤, 동일한 방식으로 equals를 구현했다고 해보자.
     * - 만일 colorPoint.equals(smellPoint)를 실행하면, 무한 재귀에 빠진다.
     * - 구체 클래스를 확장해 새로운 값을 추가하면서, equals 규약을 만족 시킬 방법은 없다.
     * */
    @Test
    public void testViolateTransitivity() {
        ColorPointViolateTransitivity x = new ColorPointViolateTransitivity(1, 2, Color.RED);
        Point y = new Point(1, 2);
        ColorPointViolateTransitivity z = new ColorPointViolateTransitivity(1, 2, Color.BLUE);
        Assertions.assertAll(
                () -> Assertions.assertTrue(x.equals(y)),
                () -> Assertions.assertTrue(y.equals(z)),
                () -> Assertions.assertFalse(x.equals(z))
        );
    }

    /** 무한재귀
     * - colorPoint와 commentPoint 모두 Point의 인스턴스이면서 자신의 인스턴스가 아니면, 상대방의 equals 함수를 통해 체크하는 부분이 있다.
     * - 따라서 계속해서 서로의 equals를 부르게 되므로, stackOverflowError가 발생하게 된다.
     *  */
    @Test
    public void testStackOverFlow() {
        ColorPointViolateTransitivity colorPoint = new ColorPointViolateTransitivity(1, 2, Color.RED);
        CommentPointDangerous commentPoint = new CommentPointDangerous(1, 2, "test");
        Assertions.assertThrows(
                StackOverflowError.class,
                () -> colorPoint.equals(commentPoint)
        );
    }

    /** 리스코프 치환 원칙 위배
     *  - 리스코프 치환원칙: 어떤 타입에 있어 중요한 속성이라면, 하위 타입에서도 마찬가지로 중요하다.
     *  - 즉, Point의 하위 클래스는 여전히 정의상 Point이므로, 어디서든 Point로써 활용되어야 한다.
     *  - 하지만, 아래의 CounterPointViolateLSP 는 PointViolateLSP 의 하위클래스임에도 불구하고, PointViolateLSP 에 있는 정적 메서드를 제대로 쓰지 못한다.
     * */
    @Test
    public void testViolateLsp() {
        PointViolateLSP p1 = new PointViolateLSP(1, 0);
        CounterPointViolateLSP p2 = new CounterPointViolateLSP(1, 0);
        PointViolateLSP.onUnitCircle(p1);
        Assertions.assertAll(
                () -> Assertions.assertTrue(PointViolateLSP.onUnitCircle(p1)), // 작동 안한다. 책에서 set 은 equals를 통해 비교한다고 가정한 부분이 달라져셔?
                () -> Assertions.assertFalse(PointViolateLSP.onUnitCircle(p2))
        );
    }

    /**
     * 컴포지트 패턴을 통해서 equals 규약 지키면서 값 추가
     * - 다만, equals의 규약을 지키면서도 상속을 사용할 수 있는 경우가 있는데, 아무런 값을 갖지 않은 추상 클래스를 활용하고
     * 이 추상 클래스의 인스턴스를 만드는 것을 막는 것이다.
     * */
    @Test
    public void testCompositeEquals() {
        ColorPointSatisfyEqualConvention cp = new ColorPointSatisfyEqualConvention(1, 2, Color.RED);
        Point p = new Point(1, 2);
    }

    /**
     * 일관성: 두 객체가 같다면, 영원히 같아야 한다.
     * */

    /**
     * null 아님: equals에서 null임을 검사할 시, null로 체크할 필요가 없다.
     * 대신 object가 특정 타입인지 아닌지를 조사해서 묵시적으로 null을 체크하는 편이 훨씬 낫다. !(o instanceof MyType)
     * */

}

// Symmetry
class ColorPointViolationSymmetry extends Point {
    private final Color color;
    public ColorPointViolationSymmetry(int x, int y, Color color) {
        super(x, y);
        this.color = color;
    }


    @Override
    public boolean equals(Object o) {
        if(!(o instanceof ColorPointViolationSymmetry))
            return false;
        return super.equals(o) && ((ColorPointViolationSymmetry) o).color == color;
    }

}

// Transitivity
class ColorPointViolateTransitivity extends Point {
    private final Color color;
    public ColorPointViolateTransitivity(int x, int y, Color color) {
        super(x, y);
        this.color = color;
    }


    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Point))
            return false;
        if(!(o instanceof ColorPointViolateTransitivity))
            return o.equals(this);
        return super.equals(o) && ((ColorPointViolateTransitivity) o).color == color;
    }
}

class CommentPointDangerous extends Point {
    private final String comment;
    public CommentPointDangerous(int x, int y, String comment) {
        super(x, y);
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Point))
            return false;
        if(!(o instanceof CommentPointDangerous))
            return o.equals(this);
        return super.equals(o) && ((CommentPointDangerous) o).comment == comment;
    }
}



enum Color {
    RED("RED"),
    BLUE("BLUE");

    Color(String color) {}
}

class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override public boolean equals(Object o) {
        if(!(o instanceof Point))
            return false;
        Point p = (Point) o;
        return p.x == x && p.y == y; // x, y 좌표가 같은지 논리적 동치성 확인
    }
}

// LSP
class PointViolateLSP {
    private final int x;
    private final int y;

    public PointViolateLSP(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private static final Set<PointViolateLSP> unitCircle = Set.of(
            new PointViolateLSP(1, 0), new PointViolateLSP(0, 1),
            new PointViolateLSP(-1, 0), new PointViolateLSP(0, -1)
    );
    public static boolean onUnitCircle(PointViolateLSP p) {
        return unitCircle.contains(p);
    }

    @Override public boolean equals(Object o) {
        System.out.println(o);
        System.out.println(o.getClass());
        if(o == null || o.getClass() != getClass()) // 인스턴스가 아니라, 같은 PointViolateLSP 경우에만 작동하도록 변경
            return false;
        PointViolateLSP p = (PointViolateLSP) o;
        return p.x == x && p.y == y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y); // 해시코드도 같이 변경해야한다.
    }
}

class CounterPointViolateLSP extends PointViolateLSP {
    private static final AtomicInteger counter = new AtomicInteger();
    public CounterPointViolateLSP(int x, int y) {
        super(x, y);
        counter.incrementAndGet();

    }
    public static int numberCreated() { return counter.get(); }
}

// 상속 대신 컴포지트를 사용해라.
class ColorPointSatisfyEqualConvention {
    private final Point point;
    private final Color color;
    public ColorPointSatisfyEqualConvention(int x, int y, Color color) {
        point = new Point(x, y);
        this.color = Objects.requireNonNull(color);
    }
    public Point asPoint() {
        return point;
    }
    @Override public boolean equals(Object o) {
        if(!(o instanceof ColorPointSatisfyEqualConvention))
            return false;
        ColorPointSatisfyEqualConvention cp = (ColorPointSatisfyEqualConvention) o;
        return cp.point.equals(point) && cp.color.equals(color);
    }
}