package com.health.tracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "http://localhost:3000")
public class AiHealthController {

    private static final Map<String, String> HEALTH_KB = new LinkedHashMap<>();

    static {
        HEALTH_KB.put("water|hydration|drink|thirsty",
                "Daily Water Intake: Drink 2-2.5 litres/day. Active people need 3-4L. Tips: start morning with water, carry a bottle, eat water-rich foods like cucumber and watermelon.");

        HEALTH_KB.put("sleep|rest|insomnia|tired|bedtime",
                "Better Sleep Tips: Adults need 7-9 hours. Avoid screens 1hr before bed, keep a consistent schedule, keep room cool, avoid caffeine after 2PM.");

        HEALTH_KB.put("bmi|body mass|overweight|obese|underweight",
                "BMI = Weight(kg) divided by Height squared(m). Under 18.5=Underweight, 18.5-24.9=Normal, 25-29.9=Overweight, 30+=Obese. Consult your doctor for guidance.");

        HEALTH_KB.put("weight|lose weight|diet|slim|deficit",
                "Weight Loss Tips: Create 300-500 kcal deficit daily. Eat lean proteins, vegetables, berries. Avoid processed foods and sugary drinks. Safe loss = 0.5-1kg per week.");

        HEALTH_KB.put("steps|walk|10000|walking",
                "Steps Guide: 10,000 steps/day is the goal but 7,000+ already gives major health benefits. Each step burns about 0.04 kcal. Take stairs, walk during calls.");

        HEALTH_KB.put("energy|fatigue|boost|sluggish|exhausted",
                "Boost Energy: Drink water first thing, eat protein and complex carbs, get morning sunlight, sleep 7-9hrs, exercise daily. Avoid sugar spikes and excessive caffeine.");

        HEALTH_KB.put("workout|exercise|gym|running|cardio",
                "Calorie Burn per 30 min: Running=400-500, Swimming=300-400, HIIT=350-450, Cycling=300-400, Weights=180-250, Yoga=120-180. Combine cardio and strength training.");

        HEALTH_KB.put("stress|anxiety|mental|relax|calm|worry",
                "Reduce Stress: Try 4-7-8 breathing (inhale 4s, hold 7s, exhale 8s). Walk in fresh air, journal, sleep well, exercise regularly, limit social media.");

        HEALTH_KB.put("protein|muscle|build|strength|nutrition",
                "Protein Needs: Sedentary=0.8g/kg, Active=1.2-1.6g/kg, Muscle building=1.6-2.2g/kg. Best sources: eggs, chicken, fish, lentils, Greek yogurt, peanut butter.");

        HEALTH_KB.put("heart|cardiovascular|blood pressure|cholesterol",
                "Heart Health: Exercise 150 min/week, eat Mediterranean diet, avoid smoking, limit alcohol, sleep 7-9hrs, manage stress. Healthy BP = below 120/80 mmHg.");

        HEALTH_KB.put("vitamin|supplement|immunity|immune",
                "Immunity Boost: Vitamin C (citrus, peppers), Vitamin D (sunlight, fish), Zinc (nuts, seeds), Probiotics (yogurt). Eat colourful vegetables every day.");

        HEALTH_KB.put("calorie|food|meal|eat|breakfast|lunch|dinner",
                "Healthy Eating: Aim for 2000 kcal/day. Fill half your plate with vegetables, quarter with protein, quarter with complex carbs. Eat every 3-4 hours.");

        HEALTH_KB.put("mood|depression|sad|happy|emotion",
                "Mood Tips: Exercise releases endorphins, even a 20 min walk helps. Get sunlight daily, maintain social connections, reduce alcohol, track your mood in the app.");

        HEALTH_KB.put("yoga|stretch|flexibility|meditation|mindfulness",
                "Yoga and Mindfulness: Just 15-20 minutes daily reduces cortisol and improves flexibility. Try Sun Salutation in the morning and child pose before bed.");
    }

    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> body) {
        String question = body.getOrDefault("message", "").toLowerCase().trim();

        Map<String, String> response = new HashMap<>();

        if (question.isEmpty()) {
            response.put("reply", "Please ask me a health question!");
            return ResponseEntity.badRequest().body(response);
        }

        for (Map.Entry<String, String> entry : HEALTH_KB.entrySet()) {
            String[] keywords = entry.getKey().split("\\|");
            for (String keyword : keywords) {
                if (question.contains(keyword.trim())) {
                    response.put("reply", entry.getValue());
                    return ResponseEntity.ok(response);
                }
            }
        }

        if (question.contains("hello") || question.contains("hi") || question.contains("hey")) {
            response.put("reply", "Hello! I am your AI Health Assistant.\nAsk me about:\n"
                    + "- Water and hydration\n"
                    + "- Sleep improvement\n"
                    + "- BMI and weight\n"
                    + "- Workouts and exercise\n"
                    + "- Nutrition and calories\n"
                    + "- Stress and mental health\n"
                    + "- Heart health\n"
                    + "- Protein and muscle building");
            return ResponseEntity.ok(response);
        }

        response.put("reply", "I can help with: water intake, sleep tips, BMI, weight loss, "
                + "steps, energy, workouts, stress, protein, heart health, vitamins, mood, and yoga.\n\n"
                + "Try asking: How do I sleep better? or Give me water intake tips.\n\n"
                + "Always consult a healthcare professional for medical advice.");
        return ResponseEntity.ok(response);
    }
}