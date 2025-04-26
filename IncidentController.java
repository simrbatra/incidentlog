package com.example.incidentlog.controller;

import com.example.incidentlog.model.Incident;
import com.example.incidentlog.repository.IncidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/incidents")
public class IncidentController {

    @Autowired
    private IncidentRepository incidentRepository;

    // GET /incidents
    @GetMapping
    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    // POST /incidents
    @PostMapping
    public ResponseEntity<Incident> createIncident(@RequestBody Incident incident) {
        if (incident.getTitle() == null || incident.getDescription() == null || incident.getSeverity() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (!incident.getSeverity().matches("(?i)Low|Medium|High")) {
            return ResponseEntity.badRequest().build();
        }

        Incident savedIncident = incidentRepository.save(incident);
        return ResponseEntity.status(201).body(savedIncident);
    }

    // GET /incidents/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Incident> getIncidentById(@PathVariable Long id) {
        Optional<Incident> incident = incidentRepository.findById(id);
        return incident.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /incidents/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncident(@PathVariable Long id) {
        if (!incidentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        incidentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
