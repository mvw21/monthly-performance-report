import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import report.Constants;
import report.Employee;
import report.ReportDefinition;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        try{
            //Read Data input from console
            List<Employee> employees = readDataInput();

            //Read Data input from files in resources package
            /*
            List<Employee> employees = readDataInputFromResources();
            */

            //Read report.ReportDefinition input
            ReportDefinition reportDefinition = readReportDefinition();

            //Store all employees in a map
            Map<Employee,Double> scoreMap = getScoreMap(employees,reportDefinition);

            // Top Performance Map where scorePerformanceThreshold <= topPerformersThreshold
            Map<Employee,Double> topPercentageScoresMap = topPerformanceScoreMap(scoreMap,reportDefinition);

            //Print Result
            printToCsv(employees,reportDefinition,topPercentageScoresMap);

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private static List<Employee> readDataInputFromResources() throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        InputStream inputStream =
                new FileInputStream(new File(Constants.DATA_PATH));

        InputStream inputStreamReport =
                new FileInputStream(new File(Constants.REPORT_DEFINITION_PATH));

        TypeReference<List<Employee>> typeReference = new TypeReference<List<Employee>>() {
        };


        return mapper.readValue(inputStream,typeReference);
    }

    private static Map<Employee, Double> topPerformanceScoreMap(Map<Employee, Double> scoreMap,ReportDefinition reportDefinition) {
        Map<Employee,Double> topPercentageScoresMap =
                scoreMap.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .limit(reportDefinition.getTopPerformersThreshold())
                        .collect(Collectors.toMap(
                                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return topPercentageScoresMap;
    }


    private static Map<Employee, Double> getScoreMap(List<Employee> employees, ReportDefinition reportDefinition) {
        Map<Employee, Double> scoreMap = new LinkedHashMap<>();

        for(Employee e : employees){
            if(!scoreMap.containsKey(e)){
                double score = 0.0;
                if(reportDefinition.isUseExprienceMultiplier()){
                    score = (double)(e.getTotalSales() / e.getSalesPeriod()) * e.getExperienceMultiplier();
                }else{
                    score +=(double)(e.getTotalSales() / e.getSalesPeriod());
                }
                scoreMap.put(e,score);
            }

        }
            return scoreMap;
    }

    private static ReportDefinition readReportDefinition() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        InputStream inputStreamReport =
                new FileInputStream(new File(Constants.REPORT_DEFINITION_PATH));
        TypeReference<List<Employee>> typeReference = new TypeReference<List<Employee>>() {
        };

        ReportDefinition reportDefinition =
                mapper.readValue(inputStreamReport,ReportDefinition.class);

        return reportDefinition;
    }

    private static List<Employee> readDataInput() throws IOException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello!");
        System.out.println("Please enter a String Data path.(Example of the format: src/main/resources/Data.json)");
        System.out.print("String Data path:");
        String dataPath = scanner.nextLine();
        System.out.print("Enter Report Definition path:");
        String reportDefinitionPath = scanner.nextLine();

        InputStream inputStream =
                new FileInputStream(new File(dataPath));

        InputStream inputStreamReport =
                new FileInputStream(new File(reportDefinitionPath));

                ObjectMapper mapper = new ObjectMapper();
      /*
        InputStream inputStream =
                new FileInputStream(new File(Constants.DATA_PATH));

        InputStream inputStreamReport =
                new FileInputStream(new File(Constants.REPORT_DEFINITION_PATH));
        */
        TypeReference<List<Employee>> typeReference = new TypeReference<List<Employee>>() {
        };

         List<Employee> employees = mapper.readValue(inputStream,typeReference);


        return employees;
    }

    private static void printToCsv(List<Employee> employees, ReportDefinition reportDefinition, Map<Employee,Double> topPercentageScoresMap) throws FileNotFoundException {
        File csvFile = new File(Constants.RESULT_CSV_PATH);
        PrintWriter pw = new PrintWriter(csvFile);
        pw.println("Name , Score");

        for(Employee e : employees){
            if(e.getSalesPeriod() <= reportDefinition.getPeriodLimit()){
                if(topPercentageScoresMap.containsKey(e)){
                    pw.printf("%s, %.1f%n",e.getName(),topPercentageScoresMap.get(e));
                }
            }
        }
        pw.close();
        System.out.println("Program finished successfully. Please check your .csv file.");
        System.out.println("Have a nice day!");
    }
}
