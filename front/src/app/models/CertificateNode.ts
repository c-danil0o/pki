import {TreeNode} from "primeng/api";
import {TreeItem} from "performant-array-to-tree";

export interface CertificateNode extends TreeItem{
  id?: string;
  parentId?: string;
  alias?: string;
  issuerName?: string;
  subjectEmail?: string;
  type?: string;
  status?: string;
  validTo?: Date;

  key?: string;
  label?: string;
  checked?: boolean;
  icon?: string;
  children?: []
}
