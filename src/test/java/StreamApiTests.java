import model.JobTitle;
import model.Users;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StreamApiTests {

    public static List<Users> getUsers() {
        return List.of(
                new Users("Alex Tsvar", new BigDecimal(1000), JobTitle.AQA),
                new Users("Ivan Ivanow", new BigDecimal(200), JobTitle.QA),
                new Users("Kurchenko Vladimir", new BigDecimal(3210), JobTitle.AQA),
                new Users("Kurchenko Vladimir", new BigDecimal(3210), JobTitle.MANAGER),
                new Users("Vlad Vladislav", new BigDecimal(999), JobTitle.DEVELOPER),
                new Users("Alexandr Homich", new BigDecimal(100500), JobTitle.CEO),
                new Users("Marina Marina", new BigDecimal(30201), JobTitle.HR)
        );
    }

    @Test(description = "Проверка сортировки по возрастанию убыванию - зарплаты")
    public void validateSortStream() {
        List<Users> sortedUsers = getUsers().stream()
                .sorted(Comparator.comparing(Users::getSalary))
                .collect(Collectors.toList());
        Assert.assertEquals(new BigDecimal(200), sortedUsers.get(0).getSalary());

        List<Users> sortedUsers2 = getUsers().stream()
                .sorted(Comparator.comparing(Users::getSalary).reversed())
                .collect(Collectors.toList());
        Assert.assertEquals(new BigDecimal(100500), sortedUsers2.get(0).getSalary());
    }

    @Test(description = "Фильтрация коллекций - фильтрация по AQA")
    public void validateFilterStream() {
        List<Users> filterUsers = getUsers().stream()
                .filter(t -> t.getJobTitle() == JobTitle.AQA)
                .collect(Collectors.toList());

        Assert.assertEquals(2, filterUsers.size());
    }

    @Test(description = "Поиск максимальной/минимальной зарплаты")
    public void validateMaxMinSalaryStream() {
        Users maxSalary = getUsers().stream()
                .max(Comparator.comparing(Users::getSalary)).orElse(null);
        Assert.assertNotNull(maxSalary);
        Assert.assertEquals("Alexandr Homich", maxSalary.getName());

        Users minSalary = getUsers().stream()
                .min(Comparator.comparing(Users::getSalary)).orElse(null);
        Assert.assertNotNull(minSalary);
        Assert.assertEquals("Ivan Ivanow", minSalary.getName());
    }

    @Test(description = "Проврека что у нас есть/нет сотвудника с именем: ")
    public void validateExistStream() {
        Assert.assertFalse(getUsers().stream().anyMatch(t -> t.getName().equals("Alex Alex")));
        Assert.assertTrue(getUsers().stream().anyMatch(t -> t.getName().equals("Ivan Ivanow")));
    }

    @Test(description = "Проверка, что всего 4 человека получают больше 1000 ")
    public void validateBySalaryStream() {
        List<Users> usersWithSalary = getUsers().stream()
                .filter(t -> t.getSalary().compareTo(BigDecimal.valueOf(1000)) > 0)
                .collect(Collectors.toList());
        Assert.assertEquals(4, usersWithSalary.size());
    }
}
