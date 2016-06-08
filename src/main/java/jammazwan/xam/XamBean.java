package jammazwan.xam;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.camel.Exchange;
import org.apache.camel.Handler;

import jammazwan.entity.City;
import jammazwan.entity.Company;
import jammazwan.entity.Employee;
import jammazwan.entity.Manager;
import jammazwan.entity.Name;
import jammazwan.entity.Shop;
import jammazwan.entity.Surname;

public class XamBean {
	/*
	 * Probably wouldn't do a bunch of static lists as production code? What
	 * could possibly go wrong? :)
	 */
	private int MAX_SIZE = 25;
	private static List<City> cities = new ArrayList<City>();
	private static List<Company> companies = new ArrayList<Company>();
	private static List<Name> names = new ArrayList<Name>();
	private static List<Surname> surnames = new ArrayList<Surname>();
	private static List<Employee> employees = new ArrayList<Employee>();

	@Handler
	public String answer(Exchange exchange) {
		Object obj = exchange.getIn().getBody();
		installObject(obj);
		return "done";
	}

	private void installObject(Object obj) {
		if (obj instanceof City) {
			if (cities.size() < MAX_SIZE) {
				cities.add((City) obj);
			}
		} else if (obj instanceof Company) {
			if (companies.size() < MAX_SIZE) {
				companies.add((Company) obj);
			}
		} else if (obj instanceof Name) {
			if (names.size() < MAX_SIZE) {
				names.add((Name) obj);
			}
		} else if (obj instanceof Surname) {
			if (surnames.size() < MAX_SIZE) {
				surnames.add((Surname) obj);
			}
		} else {
			throw new RuntimeException("OBJECT WAS " + obj.getClass().getName());
		}
	}

	public List<Employee> generateEmployees(Exchange exchange) {
		System.out.println(cities.size() + " " + companies.size() + " " + names.size() + " " + surnames.size());
		if (employees.size() < 10) {
			for (int i = 0; i < 25; i++) {
				Employee employee = new Employee(cities.get(i).getCity(), companies.get(i).getCompanyName(),
						names.get(i).getMale(), surnames.get(i).getName());
				employees.add(employee);
			}
		}
		exchange.getIn().setBody(employees);
		return employees;
	}

	public Shop generateShop(Exchange exchange) {
		generateEmployees(exchange);
		Manager manager = new Manager("Williamsburg", "Abc Consulting", "Freddy", "Applebee");
		Shop shop = new Shop(employees, manager, "1723 Dohouse Road, Williamsburg Nevada", "Abc Operations");
		exchange.getIn().setBody(shop);
		return shop;
	}

}
