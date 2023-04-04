package org.opencds.cqf.fhir.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.opencds.cqf.fhir.api.error.Errors;
import org.opencds.cqf.fhir.api.header.Headers;
import org.opencds.cqf.fhir.api.response.Response;
import org.opencds.cqf.fhir.api.response.Responses;
import org.opencds.cqf.fhir.api.status.HttpStatus;

public class ResponseTests {

  @Test
  public void bubba() {

    var result = Responses.ok(new Patient())
        .then(this::patientToEncounter)
        .then(this::encounterToError)
        .then(this::shouldBeNoop);

    assertFalse(result.isOk());
    assertTrue(result.hasError());
    assertEquals("didn't find a thing", result.error().message());
  }

  protected Encounter patientToEncounter(Patient p) {
    return new Encounter();
  }

  protected Response<Encounter> encounterToError(Encounter e) {
    return Responses.error(Errors.forMessage("didn't find a thing"),
        HttpStatus.INTERNAL_SERVER_ERROR,
        new Headers());
  }

  protected Response<Medication> shouldBeNoop(Response<Encounter> rep) {
    throw new RuntimeException("Shouldn't get here due to error propagation");
  }


}
