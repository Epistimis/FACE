# com.epistimis.face.parent
FACE UoP and Integration

The Future Airborne Capability Environment (FACE) work originally commissioned by the US Navy and eventually supported by the USAF and USArmy.  
Interest in the data definition portion generally resulted in a decision to split UDDL out of FACE for general use. This repo thus depends on 
companion repos [UDDL](https://github.com/Epistimis/com.epistimis.uddl.parent) and [UDDL Query Language](https://github.com/Epistimis/com.epistimis.uddl.query.parent).
You must include those in your build environment to build this successfully.

The [current specification is available](https://publications.opengroup.org/standards/face/c207) for free after to you create an [OpenGroup](https://www.opengroup.org) account.  This repo implements the FACE Data Model Language (Appendix J), but only the UoP (J.2.5) and Integration (J.2.6)
sections. The Traceability (J.2.7) portion is not implemented at this time. This will soon include OCL (J.6).

This code is based on that spec but is 'unofficial'. It does not use the namespace/packaging from the original spec which results in some minor modifications to OCL (J.6) . It does not use the official EMOF/ .ecore model (J.5) though the generated model should be compatible with it. Other than that, it should be functionally equivalent.

Note also that the syntax used in the project is mine alone - it is not part of the 'official' FACE spec (the FACE Template Specification  grammar is the same as the spec - but that's in a separate companion project to this one). The syntax choices here were based on following philosophy:

1. Use familiar paradigms where possible - but only if used in the same way. E.g., '{' and '}' are used for scoping just as they are in languages like C++
and Java.

2. Be succinct - every keystroke counts. Most of the keywords chosen are abbreviations of their names in the UDDL spec.

# Getting started

See the [UDDL Getting Started Guide](https://github.com/Epistimis/com.epistimis.uddl.parent/blob/0478f25fcb65772d6c26a104481c982113e73c20/GETTING_STARTED.md) for info on setting up an Eclipse development environment. It will take you all the way to a working demo.

# Security and Bug Reports
For most bugs, create a GitHub issue. If you believe you have found a security issue, see [Security[(SECURITY.md) for reporting instructions.

# User Doc
We need user Doc! A draft will be provided soon in Markdown and linked here. Stay tuned!
