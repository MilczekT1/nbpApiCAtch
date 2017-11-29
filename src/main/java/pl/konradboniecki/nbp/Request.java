package pl.konradboniecki.nbp;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Request {
    private LocalDate startDate;
    private LocalDate endDate;
    private String currency;
    
    public Request() {
    }
    
    public Request(String currency, LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.currency = currency;
    }
}
