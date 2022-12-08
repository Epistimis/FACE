package com.epistimis.face.constraints;


//Cloned from FACE 3.1 Conformance Test Suite DMVT

import com.epistimis.face.constraints.standalone.EValidatorAdapter;
import org.eclipse.emf.ecore.EValidator;

public class FACEOCLValidatorFactory
{
  public static EValidator createFaceOclValidator() {
      return (EValidator)new EValidatorAdapter();
  }
}