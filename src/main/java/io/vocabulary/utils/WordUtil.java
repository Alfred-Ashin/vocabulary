package io.vocabulary.utils;

import io.vocabulary.model.WordIndex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Ashin
 * @date 2022-05
 * @Desc 单词随机抽取字符串
 */
public class WordUtil {

    static String word;
    // 随机数范围的最大值
    static int max = 0;
    // 随机数范围的最小值
    static int min = 0;

    public static List<WordIndex> extractWordCharacters(String word) {
        // 二次赋值
        WordUtil.word = word;
        WordUtil.max = word.length();
        WordUtil.min = 0;
        List<WordIndex> rult = null;
        // 判断这个英语单词的长度
        int wordlength = word.length();
        // 对英语单词的长度进行判断
        if (wordlength <= 5) {
            // 单词数量小于或等于五个单词 抽空一个
            int n = 1;
           rult = rult(min, max, n);
        } else if (wordlength >= 6 && wordlength <= 10) {
            // 单词数量小于或等于十个单词,大于等于六个 抽空3个
            int n = 3;
            rult = rult(min, max, n);
        } else if (wordlength >= 11 && wordlength <= 15) {
            // 要随机的个数
            int n = 4;
            rult =rult(min, max, n);
        } else if (wordlength >= 16) {
            // 要随机的个数
            int n = 5;
            rult = rult(min, max, n);
        }
        return rult;
    }

    // 随机数去重
    public static int[] randomCommon(int min, int max, int n) {
        if (n > (max - min + 1) || max < min) {
            return null;
        }
        int[] result = new int[n];
        int count = 0;
        while (count < n) {
            int num = (int) (Math.random() * (max - min)) + min;
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if (num == result[j]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                result[count] = num;
                count++;
            }
        }
        return result;
    }

        // 循环出打印的次数 并且打印出来 n=抽取次数
        public static List<WordIndex> rult(int min, int max, int n) {
            List<WordIndex> list = new ArrayList<>();
        // 调用上面的随机数去重方法,并且放入 min max n三个值
            int[] randomCommon = randomCommon(min, max, n);
        // System.out.println(word + " " + “随机抽取单词为:”);
            for (int i = 0; i < n; i++) {
        // 把抽到的随机数赋值给in
                int in = randomCommon[i];
        //输出抽到的随机数下标
//                System.out.print(in + "");
        // 把抽到的随机数按下标放进要抽空的单词里面
        // char charAt = ;word.charAt(in)
        // 把抽到的随机数放进下标里面进行判断 如果为空的话再次进行抽取
                        String str = String.valueOf(word.charAt(in));
        //输出抽到的英语单词
                System.out.println("" + str);
        // 对抽到的随机数进行判断
                if (str.equals("")) {
                        System.out.println("抽到单词有空,重新抽取");
                                rult(min, max, n);
                break;
            }
                WordIndex index = new WordIndex();
                index.setIndex(in);
                index.setCharacter(String.valueOf(word.charAt(in)));

                list.add(index);
        }
        return list;
        }


}

