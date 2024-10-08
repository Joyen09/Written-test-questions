package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class TableInput {
    private List<String> headers = new ArrayList<String>();
    private List<List<String>> data = new ArrayList<>();

    public void intputHeaders(){
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("請輸入欄位標題：");
            String input = scanner.nextLine();
            if (isValidHeader(input)) {
                headers = List.of(input.split(","));
                break;
            }
        }
    }

    private boolean isValidHeader(String input) {
        // 確保標題為字母和數字組成，並且用逗號分隔，至少兩個標題
        return input.matches("([a-zA-Z0-9]+,)+[a-zA-Z0-9]+");
    }

    public void inputData() {
        Scanner scanner = new Scanner(System.in);
        boolean firstInput = true;
        while (true) {
            if (firstInput) {
                System.out.println("請輸入表格內部資料(離開請輸入:quit)：");
                firstInput = false;
            }
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("quit")) {
                if (data.isEmpty()) {
                    System.out.println("至少需要有一筆資料！");
                    System.out.println("請輸入表格內部資料(離開請輸入:quit)：");
                } else {
                    break;
                }
            } else {
                String[] values = input.split(",");
                if (values.length == headers.size()) {
                    data.add(List.of(values));
                    System.out.println("請輸入表格內部資料(離開請輸入:quit)：");
                } else {
                    System.out.println("格式錯誤，請重新輸dd入表格內部資料(離開請輸入:quit)：");
                }
            }
        }
    }

    public void searchAndDisplay() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("請輸入欲搜尋關鍵字：");
        String keyword = scanner.nextLine().trim();

        System.out.println("================================================");
        // 顯示所有標題
        System.out.println(headers);

        System.out.println("-------------------------------------");
        List<List<String>> matchedRows = data.stream()
                .filter(row -> row.stream().anyMatch(value -> value.contains(keyword)))
                .collect(Collectors.toList());

        matchedRows.forEach(System.out::println);

        if (matchedRows.isEmpty()) {
            System.out.println("沒有找到匹配的資料。");
        }
    }

    public static void main(String[] args) {
        TableInput tableInput = new TableInput();
        tableInput.intputHeaders();
        tableInput.inputData();
        tableInput.searchAndDisplay();
    }
}
