package report;

public class Employee {

        private String name;
        private Integer totalSales;
        private Integer salesPeriod;
        private Double experienceMultiplier;

        public Employee(String name, Integer totalSales, Integer salesPeriod, Double experienceMultiplier) {
                this.name = name;
                this.totalSales = totalSales;
                this.salesPeriod = salesPeriod;
                this.experienceMultiplier = experienceMultiplier;
        }

        public Employee() {
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public Integer getTotalSales() {
                return totalSales;
        }

        public void setTotalSales(Integer totalSales) {
                this.totalSales = totalSales;
        }

        public Integer getSalesPeriod() {
                return salesPeriod;
        }

        public void setSalesPeriod(Integer salesPeriod) {
                this.salesPeriod = salesPeriod;
        }

        public Double getExperienceMultiplier() {
                return experienceMultiplier;
        }

        public void setExperienceMultiplier(Double experienceMultiplier) {
                this.experienceMultiplier = experienceMultiplier;
        }

}
