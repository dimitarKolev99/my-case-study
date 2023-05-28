//package com.example.app.helpers;
//
//import com.example.app.IdentifierRepository;
//import com.example.app.dto.EIdentifier;
//import com.example.app.dto.WaggonIdentifier;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class IdentifiersHelper {
//
//    public IdentifiersHelper() {}
//
//    @Autowired
//    IdentifierRepository identifierRepository;
//
//    public List<WaggonIdentifier> findIdentifiers(List<String> identifiersList) {
//
//        List<WaggonIdentifier> waggonIdentifiers = new ArrayList<>();
//
//        if (identifiersList != null && !identifiersList.isEmpty()) {
//
//            identifiersList.forEach(ident -> {
//                switch (ident) {
//                    case "A":
//                        WaggonIdentifier aIdent = identifierRepository.findBySectionIdentifier(EIdentifier.A);
//
//                        waggonIdentifiers.add(aIdent);
//
//                    case "B":
//                        WaggonIdentifier bIdent = identifierRepository.findByWaggonIdentifier(EIdentifier.B);
//
//                        waggonIdentifiers.add(bIdent);
//
//                    case "C":
//                        WaggonIdentifier cIdent = identifierRepository.findByWaggonIdentifier(EIdentifier.C);
//
//                        waggonIdentifiers.add(cIdent);
//
//                    case "D":
//                        WaggonIdentifier dIdent = identifierRepository.findByWaggonIdentifier(EIdentifier.D);
//
//                        waggonIdentifiers.add(dIdent);
//
//                    case "E":
//                        WaggonIdentifier eIdent = identifierRepository.findByWaggonIdentifier(EIdentifier.E);
//
//                        waggonIdentifiers.add(eIdent);
//
//                    case "F":
//                        WaggonIdentifier fIdent = identifierRepository.findByWaggonIdentifier(EIdentifier.F);
//
//                        waggonIdentifiers.add(fIdent);
//
//                }
//            });
//
//        }
//
//        return waggonIdentifiers;
//
//    }
//
//}
