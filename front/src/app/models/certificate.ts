export interface Certificate {
  serialNumber: string,
  issuerName: string,
  subjectName: string,
  subjectEmail: string,
  validFrom: Date,
  validTo: Date,
  type: string,
}
