package design_pattern;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import static java.lang.Thread.sleep;

interface Subject {
	public void registerObserver(Observer o);
	public void removeObserver(Observer o);
	public void notifyObservers();
}

interface Observer {
	public void update(float temp, float humidtiy, float pressure);
}

interface DisplayElement {
	public void display();
}

@Data
class WeatherData implements Subject {
	private ArrayList observers;
	private float temperature;
	private float humidity;
	private float pressure;
	
	public WeatherData() {
		this.observers = new ArrayList();
	}
	public WeatherData(ArrayList observers) {
		this.observers = observers;
	}
	
	@Override
	public void registerObserver(Observer o) {
		observers.add(o);
	}
	
	@Override
	public void removeObserver(Observer o) {
		int i = observers.indexOf(o);
		if (i >= 0) {
			observers.remove(i);
		}
	}
	
	@Override
	public void notifyObservers() {
		for (int i = 0; i < observers.size(); i++) {
			Observer observer = (Observer) observers.get(i);
			observer.update(temperature, humidity, pressure);
		}
	}
	
	public void measurementsChanged() {
		notifyObservers();
	}
	
	public void setMeasurements(float temperature, float humidity, float pressure) {
		this.pressure = temperature;
		this.humidity = humidity;
		this.pressure = pressure;
		measurementsChanged();
	}
}

class CurrentConditionDisplay implements Observer, DisplayElement {
	private float temperature;
	private float humidity;
	private Subject weatherData;
	
	public CurrentConditionDisplay(Subject weatherData) {
		this.weatherData = weatherData;
		weatherData.registerObserver(this);
	}
	
	@Override
	// NOTE: 1 Observer被push 模式，通过 setXXX() 方法
	public void update(float temp, float humidity, float pressure) {
		this.temperature = temp;
		this.humidity = humidity;
		display();
	}

	// NOTE: 2 Observer被pull 模式，通过 setXXX() 方法
	public void update(Observer obs, Object arg) {
		if (obs instanceof WeatherData) {
			WeatherData observer = (WeatherData) obs;
			this.temperature = observer.getTemperature();
			this.humidity = observer.getTemperature();
			display();
		}
	}
	
	@Override
	public void display() {
		System.out.println(hashCode() + " Current conditions: " + temperature + " F degrees and " + humidity + " % humidity");
	}
}

/**
 * 1304836502 Current conditions: 0.0 F degrees and 90.0 % humidity
 * 225534817 Current conditions: 0.0 F degrees and 90.0 % humidity
 *
 * 1304836502 Current conditions: 0.0 F degrees and 80.0 % humidity
 * 225534817 Current conditions: 0.0 F degrees and 80.0 % humidity
 */
public class M2_ObserverPattern {
	public static void main(String[] args) {
		try {
			WeatherData weatherData = new WeatherData();
			CurrentConditionDisplay current1 = new CurrentConditionDisplay(weatherData);
			CurrentConditionDisplay current2 = new CurrentConditionDisplay(weatherData);
			weatherData.setMeasurements(80, 90, 100);
			
			sleep(3000);
			System.out.println();
			weatherData.setMeasurements(40, 80, 100);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
