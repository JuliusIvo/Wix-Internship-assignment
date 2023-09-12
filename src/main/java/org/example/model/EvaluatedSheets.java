package org.example.model;

import java.util.List;

public record EvaluatedSheets(String email, List<Sheet> results) { }
