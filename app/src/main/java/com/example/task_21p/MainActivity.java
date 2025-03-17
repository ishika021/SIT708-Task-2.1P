package com.example.task_21p;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // UI components
    private Spinner categorySpinner;
    private Spinner fromUnitSpinner;
    private Spinner toUnitSpinner;
    private EditText inputEditText;
    private Button convertButton;
    private TextView resultTextView;

    // Conversion categories and units
    private final String[] categories = {"Length", "Weight", "Temperature"};
    private final String[] lengthUnits = {"Centimeter", "Inch", "Foot", "Yard", "Mile", "Kilometer"};
    private final String[] weightUnits = {"Gram", "Kilogram", "Ounce", "Pound", "Ton"};
    private final String[] temperatureUnits = {"Celsius", "Fahrenheit", "Kelvin"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        categorySpinner = findViewById(R.id.categorySpinner);
        fromUnitSpinner = findViewById(R.id.fromUnitSpinner);
        toUnitSpinner = findViewById(R.id.toUnitSpinner);
        inputEditText = findViewById(R.id.inputEditText);
        convertButton = findViewById(R.id.convertButton);
        resultTextView = findViewById(R.id.resultTextView);

        // Setup category spinner
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        // Setup unit spinners based on category selection
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateUnitSpinners(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Setup convert button click listener
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convert();
            }
        });

        // Initialize with Length units
        updateUnitSpinners(0);
    }

    private void updateUnitSpinners(int categoryPosition) {
        String[] units;
        switch (categoryPosition) {
            case 0:
                units = lengthUnits;
                break;
            case 1:
                units = weightUnits;
                break;
            case 2:
                units = temperatureUnits;
                break;
            default:
                units = new String[0];
                break;
        }

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        fromUnitSpinner.setAdapter(unitAdapter);
        toUnitSpinner.setAdapter(unitAdapter);

        // Reset result
        resultTextView.setText("");
        inputEditText.getText().clear();
    }

    private void convert() {
        String inputStr = inputEditText.getText().toString();

        // Validation
        if (inputStr.isEmpty()) {
            Toast.makeText(this, "Please enter a value to convert", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double inputValue = Double.parseDouble(inputStr);
            int categoryPosition = categorySpinner.getSelectedItemPosition();
            String fromUnit = fromUnitSpinner.getSelectedItem().toString();
            String toUnit = toUnitSpinner.getSelectedItem().toString();

            // Check if source and destination units are the same
            if (fromUnit.equals(toUnit)) {
                resultTextView.setText(inputStr);
                return;
            }

            double result;
            switch (categoryPosition) {
                case 0:
                    result = convertLength(inputValue, fromUnit, toUnit);
                    break;
                case 1:
                    result = convertWeight(inputValue, fromUnit, toUnit);
                    break;
                case 2:
                    result = convertTemperature(inputValue, fromUnit, toUnit);
                    break;
                default:
                    result = 0.0;
                    break;
            }

            // Format result to 4 decimal places
            resultTextView.setText(String.format("%.4f", result));

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
        }
    }

    private double convertLength(double value, String fromUnit, String toUnit) {
        // Convert to cm first
        double inCm;
        switch (fromUnit) {
            case "Centimeter":
                inCm = value;
                break;
            case "Inch":
                inCm = value * 2.54;
                break;
            case "Foot":
                inCm = value * 30.48;
                break;
            case "Yard":
                inCm = value * 91.44;
                break;
            case "Mile":
                inCm = value * 160934.0;
                break;
            case "Kilometer":
                inCm = value * 100000.0;
                break;
            default:
                inCm = value;
                break;
        }

        // Convert from cm to target unit
        switch (toUnit) {
            case "Centimeter":
                return inCm;
            case "Inch":
                return inCm / 2.54;
            case "Foot":
                return inCm / 30.48;
            case "Yard":
                return inCm / 91.44;
            case "Mile":
                return inCm / 160934.0;
            case "Kilometer":
                return inCm / 100000.0;
            default:
                return inCm;
        }
    }

    private double convertWeight(double value, String fromUnit, String toUnit) {
        // Convert to grams first
        double inGrams;
        switch (fromUnit) {
            case "Gram":
                inGrams = value;
                break;
            case "Kilogram":
                inGrams = value * 1000.0;
                break;
            case "Ounce":
                inGrams = value * 28.3495;
                break;
            case "Pound":
                inGrams = value * 453.592;
                break;
            case "Ton":
                inGrams = value * 907185.0;
                break;
            default:
                inGrams = value;
                break;
        }

        // Convert from grams to target unit
        switch (toUnit) {
            case "Gram":
                return inGrams;
            case "Kilogram":
                return inGrams / 1000.0;
            case "Ounce":
                return inGrams / 28.3495;
            case "Pound":
                return inGrams / 453.592;
            case "Ton":
                return inGrams / 907185.0;
            default:
                return inGrams;
        }
    }

    private double convertTemperature(double value, String fromUnit, String toUnit) {
        // First convert to Celsius
        double inCelsius;
        switch (fromUnit) {
            case "Celsius":
                inCelsius = value;
                break;
            case "Fahrenheit":
                inCelsius = (value - 32) / 1.8;
                break;
            case "Kelvin":
                inCelsius = value - 273.15;
                break;
            default:
                inCelsius = value;
                break;
        }

        // Then convert from Celsius to target unit
        switch (toUnit) {
            case "Celsius":
                return inCelsius;
            case "Fahrenheit":
                return (inCelsius * 1.8) + 32;
            case "Kelvin":
                return inCelsius + 273.15;
            default:
                return inCelsius;
        }
    }
}