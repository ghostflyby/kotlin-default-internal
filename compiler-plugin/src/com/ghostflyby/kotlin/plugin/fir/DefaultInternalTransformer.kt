package com.ghostflyby.kotlin.plugin.fir

import org.jetbrains.kotlin.KtFakeSourceElementKind
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.Visibilities
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.getContainingClassSymbol
import org.jetbrains.kotlin.fir.analysis.checkers.hasModifier
import org.jetbrains.kotlin.fir.copyWithNewDefaults
import org.jetbrains.kotlin.fir.declarations.*
import org.jetbrains.kotlin.fir.declarations.utils.isCompanion
import org.jetbrains.kotlin.fir.declarations.utils.isInterface
import org.jetbrains.kotlin.fir.declarations.utils.visibility
import org.jetbrains.kotlin.fir.extensions.FirStatusTransformerExtension
import org.jetbrains.kotlin.fir.symbols.SymbolInternals
import org.jetbrains.kotlin.fir.symbols.impl.FirClassLikeSymbol
import org.jetbrains.kotlin.lexer.KtTokens

class DefaultInternalTransformer(session: FirSession) : FirStatusTransformerExtension(session) {
  @OptIn(SymbolInternals::class)
  override fun needTransformStatus(declaration: FirDeclaration): Boolean {
    if (declaration !is FirMemberDeclaration)
      return false

    val containingClass = declaration.getContainingClassSymbol()?.fir
    if (containingClass is FirRegularClass && containingClass.isInterface) {
      return false
    }

    if (declaration is FirRegularClass && declaration.isCompanion) {
      return false
    }

    if (declaration is FirConstructor) {
      return false
    }

    return declaration.run {
      origin == FirDeclarationOrigin.Source &&
          !hasModifier(KtTokens.PUBLIC_KEYWORD)
          && (visibility == Visibilities.Unknown || visibility == Visibilities.Public)
    }
  }

  override fun transformStatus(status: FirDeclarationStatus, declaration: FirDeclaration): FirDeclarationStatus {
    return status.copyWithNewDefaults(
      visibility = Visibilities.Internal
    )
  }

  override fun transformStatus(
    status: FirDeclarationStatus,
    propertyAccessor: FirPropertyAccessor,
    containingClass: FirClassLikeSymbol<*>?,
    containingProperty: FirProperty?,
    isLocal: Boolean
  ): FirDeclarationStatus {
    return if (containingProperty != null)
      status.copyWithNewDefaults(
        visibility = containingProperty.visibility
      )
    else
      status
  }

  @OptIn(SymbolInternals::class)
  override fun transformStatus(
    status: FirDeclarationStatus,
    property: FirProperty,
    containingClass: FirClassLikeSymbol<*>?,
    isLocal: Boolean
  ): FirDeclarationStatus {
    when (val fir = containingClass?.fir) {
      is FirRegularClass -> {
        if (fir.classKind == ClassKind.ANNOTATION_CLASS)
          return status
      }
      else -> {}
    }
    return if (property.source?.kind == KtFakeSourceElementKind.PropertyFromParameter && containingClass?.visibility == Visibilities.Public) {
      status
    } else
      transformStatus(status, property)
  }

}
