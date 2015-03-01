package com.lab1;

public enum Alphabet {
    a(0),
    b(1),
    c(2),
    d(3),
    e(4),
    f(5),
    g(6),
    h(7),
    i(8),
    j(9),
    k(10),
    l(11),
    m(12),
    n(13),
    o(14),
    p(15),
    q(16),
    r(17),
    s(18),
    t(19),
    u(20),
    v(21),
    w(22),
    x(23),
    y(24),
    z(25),
    space(26),
    ъ(27),
    mod(28);

    Alphabet(int value) {
        this.value = value;
    }

    private int value;

    public int getValue() {
        return value;
    }

    public static String getString(int i) {
        for (Alphabet alphabet : Alphabet.values()) {
            if (alphabet.getValue() == i) {
                return alphabet.toString();
            }
        }
        return null;
    }
}
