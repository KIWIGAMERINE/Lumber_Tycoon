package me.kiwigamerine.lumbertycoon.EnchantmentAPI.service;

import org.apache.commons.lang.Validate;

public class ERomanNumeral {
    public ERomanNumeral() {
    }

    public static String numeralOf(int value) {
        Validate.isTrue(value > 0, "Roman numbers can't express zero or negative numbers!");
        StringBuilder builder = new StringBuilder();
        RomanNumber[] romanNumbers = ERomanNumeral.RomanNumber.values();

        for(int i = 0; i < romanNumbers.length; ++i) {
            RomanNumber romanNumber = romanNumbers[i];

            while(value >= romanNumber.getInDecimal()) {
                value -= romanNumber.getInDecimal();
                builder.append(romanNumber.name());
            }

            if (i < romanNumbers.length - 1) {
                int index = i - i % 2 + 2;
                RomanNumber subtractNum = romanNumbers[index];
                if (value >= romanNumber.getInDecimal() - subtractNum.getInDecimal()) {
                    value -= romanNumber.getInDecimal() - subtractNum.getInDecimal();
                    builder.append(subtractNum.name());
                    builder.append(romanNumber.name());
                }
            }
        }

        return builder.toString();
    }

    public static int getValueOf(String romanNumeral) {
        char[] numerals = romanNumeral.toCharArray();
        int total = 0;

        for(int i = 0; i < numerals.length; ++i) {
            int value = getNumeralValue(numerals[i]);
            if (i < numerals.length - 1 && getNumeralValue(numerals[i + 1]) > value) {
                value = -value;
            }

            if (value == 0) {
                return 0;
            }

            total += value;
        }

        return total;
    }

    public static int getNumeralValue(char numeral) {
        String romanNumeral = ("" + numeral).toUpperCase();

        try {
            RomanNumber romanNumber = ERomanNumeral.RomanNumber.valueOf(romanNumeral);
            return romanNumber.getInDecimal();
        } catch (IllegalArgumentException var3) {
            return 0;
        }
    }

    private static enum RomanNumber {
        M(1000),
        D(500),
        C(100),
        L(50),
        X(10),
        V(5),
        I(1);

        private final int valueInDec;

        private RomanNumber(int decimal) {
            this.valueInDec = decimal;
        }

        public int getInDecimal() {
            return this.valueInDec;
        }
    }
}