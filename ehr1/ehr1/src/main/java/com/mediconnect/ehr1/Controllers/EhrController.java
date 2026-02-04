package com.mediconnect.ehr1.Controllers;

import com.mediconnect.ehr1.Models.JsonDetails.DiagnosisDetails;
import com.mediconnect.ehr1.Services.EhrService;
import com.mediconnect.ehr1.dto.EhrUpdateDto;
import com.mediconnect.ehr1.dto.PatientEhrDataDto;
import com.mediconnect.ehr1.dto.PatientSummaryDto;
import com.mediconnect.ehr1.dto.UpdateStatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ehr")
@CrossOrigin(origins = "*") // Allows your frontend to call this API without CORS errors
public class EhrController {

    @Autowired
    private EhrService ehrService;
    // @Autowired
    // private JdbcTemplate jdbcTemplate;

    @GetMapping("/dashboard")
    public ResponseEntity<List<PatientSummaryDto>> getDashboardData(@RequestParam Long doctorId) {
        List<PatientSummaryDto> data = ehrService.getDoctorDashboard(doctorId);
        return ResponseEntity.ok(data);
    }
    // @GetMapping("/doctors")
    // public ResponseEntity<List<Map<String, Object>>> getDoctors() {
    //     // This runs the raw SQL and returns a list of maps (column name -> value)
    //     String sql = "SELECT * FROM doctors";
    //     List<Map<String, Object>> doctors = jdbcTemplate.queryForList(sql);
    //     return ResponseEntity.ok(doctors);
    // }

//
// @GetMapping("/list")
// public ResponseEntity<List<Map<String, Object>>> getDoctorList() {
//     // This fetches all doctors so you can pick the "active" one
//     return ResponseEntity.ok(jdbcTemplate.queryForList("SELECT id, full_name, specialization FROM doctors"));
// }
    @PutMapping("update-status")
    public  ResponseEntity<String> staus(@RequestBody UpdateStatusDto request){
        ehrService.updateStatus(request);
       return ResponseEntity.ok("Status updated Successfully");
    }

    @GetMapping("/patient-ehr")
    public ResponseEntity<PatientEhrDataDto> patientEhr(@RequestParam Long patientId){
        PatientEhrDataDto data =  ehrService.getPatientEhr(patientId);
        return ResponseEntity.ok(data);
    }

    @PostMapping("/upsert-record")
    public ResponseEntity<String> diagnosis(@RequestBody EhrUpdateDto updatedData){
        ehrService.updateRecord(updatedData);

        String action = (updatedData.getRecordId() != null) ? "Updated" : "Created";
        return ResponseEntity.ok("Record " + action + " Successfully");
    }

}


