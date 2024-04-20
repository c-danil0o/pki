export interface Certificate {
  serialNumber: string,
  issuerSerialNumber: string;
  issuerName: string,
  subjectName: string,
  subjectEmail: string,
  validFrom: Date,
  validTo: Date,
  type: string,
  status: string;
  signatureAlgorithm: string;
  alias: string;
  extensions: object
}
